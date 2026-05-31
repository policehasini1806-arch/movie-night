// =============================================
// join-room.js
// =============================================

const API = 'http://localhost:8080/api';

async function joinRoom() {
    const roomCode = document.getElementById('roomCode').value.trim().toUpperCase();
    const name     = document.getElementById('yourName').value.trim();
    const errEl    = document.getElementById('error-msg');

    errEl.classList.add('hidden');
    if (!roomCode || roomCode.length !== 6) return showError('Please enter a valid 6-character room code.');
    if (!name)                              return showError('Please enter your name.');

    const btn = document.getElementById('joinBtn');
    btn.disabled = true;
    btn.textContent = 'Joining...';

    try {
        const res = await fetch(`${API}/participants/join`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ roomCode, name })
        });

        const data = await res.json();
        if (!res.ok) throw new Error(data.error || 'Failed to join room');

        // Store session
        sessionStorage.setItem('participantId',   data.participantId);
        sessionStorage.setItem('participantName', data.participantName);
        sessionStorage.setItem('roomCode',        data.roomCode);
        sessionStorage.setItem('roomName',        data.roomName);

        showToast(`Joined "${data.roomName}"! 🎬`, 'success');

        setTimeout(() => {
            window.location.href = 'vote.html';
        }, 800);

    } catch (err) {
        showError(err.message);
    } finally {
        btn.disabled = false;
        btn.textContent = '🎬 Join Room';
    }
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

document.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') joinRoom();
});
