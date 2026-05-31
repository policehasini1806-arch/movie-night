// =============================================
// create-room.js
// =============================================

const API = 'http://localhost:8080/api';

async function createRoom() {
    const roomName = document.getElementById('roomName').value.trim();
    const hostName = document.getElementById('hostName').value.trim();
    const errEl    = document.getElementById('error-msg');

    // Validate
    errEl.classList.add('hidden');
    if (!roomName) return showError('Please enter a room name.');
    if (!hostName) return showError('Please enter your name.');

    const btn = document.getElementById('createBtn');
    btn.disabled = true;
    btn.textContent = 'Creating...';

    try {
        const res = await fetch(`${API}/rooms/create`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ roomName, hostName })
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Failed to create room');

        // Store session
        sessionStorage.setItem('participantId',   data.participantId);
        sessionStorage.setItem('participantName', data.participantName);
        sessionStorage.setItem('roomCode',        data.roomCode);
        sessionStorage.setItem('roomName',        data.roomName);

        // Show created step
        document.getElementById('displayCode').textContent     = data.roomCode;
        document.getElementById('displayRoomName').textContent = data.roomName;

        const list = document.getElementById('participantList');
        list.innerHTML = `
            <li>
                <div class="avatar">${data.participantName[0].toUpperCase()}</div>
                ${data.participantName} <span class="muted">(Host)</span>
            </li>`;

        document.getElementById('step-form').classList.add('hidden');
        document.getElementById('step-created').classList.remove('hidden');

        showToast('Room created! 🎬', 'success');

    } catch (err) {
        showError(err.message);
    } finally {
        btn.disabled = false;
        btn.textContent = '🎬 Create Room';
    }
}

function copyCode() {
    const code = document.getElementById('displayCode').textContent;
    navigator.clipboard.writeText(code).then(() => showToast('Code copied! 📋', 'success'));
}

function proceedToVote() {
    window.location.href = 'vote.html';
}

function showError(msg) {
    const el = document.getElementById('error-msg');
    el.textContent = msg;
    el.classList.remove('hidden');
}

function showToast(msg, type = '') {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.className = `toast ${type} show`;
    setTimeout(() => t.classList.remove('show'), 2800);
}

// Enter key support
document.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
        const step = document.getElementById('step-form');
        if (!step.classList.contains('hidden')) createRoom();
    }
});
