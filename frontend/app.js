(() => {
  const LS_KEY = "smartHomeApiBase";

  /** @type {number | null} */
  let selectedId = null;
  /** @type {any | null} */
  let selectedDetail = null;

  const $ = (id) => document.getElementById(id);

  function apiBase() {
    const v = localStorage.getItem(LS_KEY);
    if (v !== null && v.trim() !== "") return v.trim().replace(/\/$/, "");
    return "";
  }

  function logLine(title, body) {
    const el = $("log");
    const t = new Date().toISOString().slice(11, 19);
    const chunk =
      `[${t}] ${title}\n` + (body !== undefined ? (typeof body === "string" ? body : JSON.stringify(body, null, 2)) : "") + "\n\n";
    el.textContent = chunk + el.textContent;
    const lines = el.textContent.split("\n");
    if (lines.length > 200) el.textContent = lines.slice(0, 200).join("\n");
  }

  async function api(path, opts = {}) {
    const url = apiBase() + path;
    const headers = { ...(opts.headers || {}) };
    const hasBody = opts.body != null && opts.body !== "";
    if (hasBody) headers["Content-Type"] = "application/json";
    const res = await fetch(url, {
      ...opts,
      headers,
    });
    const text = await res.text();
    let data;
    try {
      data = text ? JSON.parse(text) : null;
    } catch {
      data = text;
    }
    if (!res.ok) {
      const err = new Error(`${res.status} ${res.statusText}`);
      err.body = data;
      throw err;
    }
    return data;
  }

  function escapeHtml(s) {
    if (s == null) return "";
    const d = document.createElement("div");
    d.textContent = String(s);
    return d.innerHTML;
  }

  async function loadDevices() {
    const list = await api("/devices");
    const ul = $("deviceList");
    ul.innerHTML = "";
    for (const d of list) {
      const li = document.createElement("li");
      li.dataset.id = String(d.id);
      if (d.id === selectedId) li.classList.add("selected");
      li.innerHTML = `<span><strong>${escapeHtml(d.name)}</strong> <span class="badge">${escapeHtml(d.type)}</span> #${d.id}</span><span class="badge">${d.autoMode ? "авто" : "ручн."} · ${d.enabled ? "вкл" : "выкл"}</span>`;
      li.addEventListener("click", () => selectDevice(d.id));
      ul.appendChild(li);
    }
    logLine("GET /devices", list);
  }

  async function selectDevice(id) {
    selectedId = id;
    selectedDetail = await api(`/devices/${id}`);
    document.querySelectorAll(".device-list li").forEach((li) => {
      li.classList.toggle("selected", Number(li.dataset.id) === id);
    });
    renderDeviceDetail();
    await loadSchedules();
    $("scheduleFormRow").style.display = "flex";
    $("scheduleHint").textContent = `Устройство #${id}`;
    logLine(`GET /devices/${id}`, selectedDetail);
  }

  function renderDeviceDetail() {
    const el = $("deviceDetail");
    if (!selectedDetail) {
      el.innerHTML = "";
      return;
    }
    const d = selectedDetail;
    const h = d.heater;
    const b = d.blinds;
    let controls = "";
    if (d.type === "HEATER" && h) {
      const working = h.working === true || h.isWorking === true;
      controls = `
        <h3 style="margin:0.75rem 0 0.5rem;font-size:13px;color:var(--muted)">Обогреватель</h3>
        <div class="row">
          <label>targetTemp <input type="number" id="hTemp" step="0.1" value="${h.targetTemp}" /></label>
          <button type="button" id="btnHeaterTemp">PATCH температура</button>
        </div>
        <div class="row">
          <label><input type="checkbox" id="hWork" ${working ? "checked" : ""} /> isWorking</label>
          <button type="button" id="btnHeaterWork">PATCH working</button>
        </div>`;
    }
    if (d.type === "BLINDS" && b) {
      controls = `
        <h3 style="margin:0.75rem 0 0.5rem;font-size:13px;color:var(--muted)">Жалюзи</h3>
        <div class="row">
          <label>position 0–100 <input type="number" id="bPos" min="0" max="100" value="${b.position ?? 0}" /></label>
          <button type="button" id="btnBlindsPos">PATCH position</button>
        </div>
        <div class="row">
          <label>openTime (HH:mm) <input type="text" id="bOpen" value="${b.openTime ?? ""}" placeholder="07:00" /></label>
          <label>closeTime <input type="text" id="bClose" value="${b.closeTime ?? ""}" placeholder="21:00" /></label>
          <button type="button" id="btnBlindsSch">PATCH schedule</button>
        </div>`;
    }
    el.innerHTML = `
      <h3 style="margin:0.75rem 0 0.5rem;font-size:13px;color:var(--muted)">Выбрано: ${escapeHtml(d.name)} (#${d.id})</h3>
      <div class="row">
        <label><input type="checkbox" id="devAuto" ${d.autoMode ? "checked" : ""} /> autoMode (погода)</label>
        <label><input type="checkbox" id="devEn" ${d.enabled ? "checked" : ""} /> enabled</label>
        <button type="button" id="btnDevPatch">Сохранить флаги</button>
        <button type="button" id="btnDevDel" class="danger">Удалить устройство</button>
      </div>
      ${controls}`;
    $("btnDevPatch").addEventListener("click", patchDeviceFlags);
    $("btnDevDel").addEventListener("click", deleteDevice);
    const ht = $("btnHeaterTemp");
    if (ht) ht.addEventListener("click", patchHeaterTemp);
    const hw = $("btnHeaterWork");
    if (hw) hw.addEventListener("click", patchHeaterWork);
    const bp = $("btnBlindsPos");
    if (bp) bp.addEventListener("click", patchBlindsPos);
    const bs = $("btnBlindsSch");
    if (bs) bs.addEventListener("click", patchBlindsSchedule);
  }

  async function patchDeviceFlags() {
    if (!selectedId) return;
    const body = {
      autoMode: $("devAuto").checked,
      enabled: $("devEn").checked,
    };
    selectedDetail = await api(`/devices/${selectedId}`, { method: "PATCH", body: JSON.stringify(body) });
    logLine(`PATCH /devices/${selectedId}`, selectedDetail);
    renderDeviceDetail();
    await loadDevices();
  }

  async function deleteDevice() {
    if (!selectedId || !confirm(`Удалить устройство #${selectedId}?`)) return;
    await api(`/devices/${selectedId}`, { method: "DELETE" });
    logLine(`DELETE /devices/${selectedId}`, "204");
    selectedId = null;
    selectedDetail = null;
    $("deviceDetail").innerHTML = "";
    $("scheduleTable").innerHTML = "";
    $("scheduleFormRow").style.display = "none";
    $("scheduleHint").textContent = "Сначала выберите устройство в списке выше.";
    await loadDevices();
  }

  async function patchHeaterTemp() {
    if (!selectedId) return;
    const targetTemp = Number($("hTemp").value);
    const data = await api(`/heaters/${selectedId}/temperature`, {
      method: "PATCH",
      body: JSON.stringify({ targetTemp }),
    });
    logLine(`PATCH /heaters/${selectedId}/temperature`, data);
    await selectDevice(selectedId);
  }

  async function patchHeaterWork() {
    if (!selectedId) return;
    const isWorking = $("hWork").checked;
    const data = await api(`/heaters/${selectedId}/working`, {
      method: "PATCH",
      body: JSON.stringify({ isWorking }),
    });
    logLine(`PATCH /heaters/${selectedId}/working`, data);
    await selectDevice(selectedId);
  }

  async function patchBlindsPos() {
    if (!selectedId) return;
    const position = Number($("bPos").value);
    const data = await api(`/blinds/${selectedId}/position`, {
      method: "PATCH",
      body: JSON.stringify({ position }),
    });
    logLine(`PATCH /blinds/${selectedId}/position`, data);
    await selectDevice(selectedId);
  }

  async function patchBlindsSchedule() {
    if (!selectedId) return;
    const openTime = $("bOpen").value.trim() || null;
    const closeTime = $("bClose").value.trim() || null;
    const body = {};
    if (openTime) body.openTime = openTime;
    if (closeTime) body.closeTime = closeTime;
    const data = await api(`/blinds/${selectedId}/schedule`, {
      method: "PATCH",
      body: JSON.stringify(body),
    });
    logLine(`PATCH /blinds/${selectedId}/schedule`, data);
    await selectDevice(selectedId);
  }

  async function loadSchedules() {
    if (!selectedId) return;
    const rows = await api(`/devices/${selectedId}/schedules`);
    const div = $("scheduleTable");
    if (!rows.length) {
      div.innerHTML = "<p class=\"sub\">Нет записей расписания.</p>";
      logLine(`GET /devices/${selectedId}/schedules`, rows);
      return;
    }
    let html =
      "<table><thead><tr><th>id</th><th>время</th><th>действие</th><th>value</th><th>активно</th><th></th></tr></thead><tbody>";
    for (const r of rows) {
      html += `<tr>
        <td>${r.id}</td>
        <td>${escapeHtml(String(r.executionTime))}</td>
        <td>${escapeHtml(r.action)}</td>
        <td>${r.targetValue ?? "—"}</td>
        <td>${r.active}</td>
        <td>
          <button type="button" class="secondary sch-toggle" data-id="${r.id}" data-active="${r.active}">${r.active ? "Выкл" : "Вкл"}</button>
          <button type="button" class="danger sch-del" data-id="${r.id}">Удалить</button>
        </td>
      </tr>`;
    }
    html += "</tbody></table>";
    div.innerHTML = html;
    div.querySelectorAll(".sch-del").forEach((btn) =>
      btn.addEventListener("click", () => deleteSchedule(Number(btn.dataset.id))),
    );
    div.querySelectorAll(".sch-toggle").forEach((btn) =>
      btn.addEventListener("click", () =>
        toggleSchedule(Number(btn.dataset.id), btn.dataset.active !== "true"),
      ),
    );
    logLine(`GET /devices/${selectedId}/schedules`, rows);
  }

  async function deleteSchedule(sid) {
    if (!confirm(`Удалить расписание #${sid}?`)) return;
    await api(`/schedules/${sid}`, { method: "DELETE" });
    logLine(`DELETE /schedules/${sid}`, "204");
    await loadSchedules();
  }

  async function toggleSchedule(sid, active) {
    const data = await api(`/schedules/${sid}`, {
      method: "PATCH",
      body: JSON.stringify({ active }),
    });
    logLine(`PATCH /schedules/${sid}`, data);
    await loadSchedules();
  }

  function normalizeHhMm(raw) {
    const t = raw.trim();
    const m = t.match(/^(\d{1,2}):(\d{2})$/);
    if (!m) return t;
    const h = Math.min(23, Math.max(0, parseInt(m[1], 10)));
    const min = Math.min(59, Math.max(0, parseInt(m[2], 10)));
    return `${String(h).padStart(2, "0")}:${String(min).padStart(2, "0")}`;
  }

  async function addSchedule() {
    if (!selectedId) return;
    const executionTime = normalizeHhMm($("schTime").value);
    if (!executionTime || !/^\d{2}:\d{2}$/.test(executionTime)) {
      alert("Время в формате чч:мм, например 8:00 или 08:30");
      return;
    }
    const action = $("schAction").value;
    const valRaw = $("schValue").value.trim();
    const targetValue = valRaw === "" ? null : Number(valRaw);
    const active = $("schActive").value === "true";
    const body = { executionTime, action, active };
    if (targetValue !== null && !Number.isNaN(targetValue)) body.targetValue = targetValue;
    const data = await api(`/devices/${selectedId}/schedules`, {
      method: "POST",
      body: JSON.stringify(body),
    });
    logLine(`POST /devices/${selectedId}/schedules`, data);
    await loadSchedules();
  }

  function init() {
    $("apiBase").value = localStorage.getItem(LS_KEY) || "";
    $("saveApiBase").addEventListener("click", () => {
      const v = $("apiBase").value.trim();
      if (v) localStorage.setItem(LS_KEY, v);
      else localStorage.removeItem(LS_KEY);
      logLine("apiBase", v || "(пусто → относительные URL)");
    });
    $("btnRefreshDevices").addEventListener("click", () => loadDevices().catch((e) => logLine("Ошибка", String(e) + (e.body ? "\n" + JSON.stringify(e.body) : ""))));
    $("btnCreateDevice").addEventListener("click", async () => {
      const name = $("newDeviceName").value.trim();
      const type = $("newDeviceType").value;
      if (!name) {
        alert("Имя устройства");
        return;
      }
      try {
        const created = await api("/devices", {
          method: "POST",
          body: JSON.stringify({ name, type }),
        });
        logLine("POST /devices", created);
        $("newDeviceName").value = "";
        await loadDevices();
        await selectDevice(created.id);
      } catch (e) {
        logLine("Ошибка POST /devices", e.body || String(e));
        alert("Ошибка: " + (e.body ? JSON.stringify(e.body) : e.message));
      }
    });
    $("btnAddSchedule").addEventListener("click", () => addSchedule().catch((e) => logLine("Ошибка", e.body || String(e))));
    $("btnWeather").addEventListener("click", async () => {
      const lat = $("wLat").value;
      const lon = $("wLon").value;
      try {
        const data = await api(`/weather/current?lat=${encodeURIComponent(lat)}&lon=${encodeURIComponent(lon)}`);
        logLine("GET /weather/current", data);
      } catch (e) {
        logLine("Ошибка weather", e.body || String(e));
      }
    });
    loadDevices().catch((e) => logLine("Ошибка", String(e)));
  }

  init();
})();
