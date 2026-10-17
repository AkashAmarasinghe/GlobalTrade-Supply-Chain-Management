(function(){
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuBtn = document.getElementById('menuBtn');

    if(menuBtn && sidebar && overlay){
        menuBtn.addEventListener('click', () => {
            sidebar.classList.add('open');
            overlay.classList.add('show');
        });
        overlay.addEventListener('click', () => {
            sidebar.classList.remove('open');
            overlay.classList.remove('show');
        });
    }

    const path = window.location.pathname.toLowerCase();
    document.querySelectorAll('.nav-link').forEach(link => {
        const key = link.dataset.nav;
        if(!key) return;
        const match =
            (key === 'dashboard' && (path.endsWith('/dashboard') || path.endsWith('/dashboard.jsp'))) ||
            (key === 'shipments' && (path.endsWith('/shipments') || path.includes('/shipments/'))) ||
            (key === 'inventory' && (path.endsWith('/inventory') || path.includes('/inventory/'))) ||
            (key === 'vendors' && (path.endsWith('/vendors') || path.includes('/vendors/') || path.endsWith('/vendor-performance'))) ||
            (key === 'customs' && (path.endsWith('/customs') || path.includes('/customs/'))) ||
            (key === 'audit' && path.endsWith('/audit')) ||
            (key === 'security' && (path.endsWith('/access-control') || path.includes('/security/'))) ||
            (key === 'monitoring' && (path.endsWith('/monitoring') || path.includes('/monitoring/'))) ||
            (key === 'route' && (path.endsWith('/route-optimization') || path.includes('/route/')));
        if(match) link.classList.add('active');
    });

    document.querySelectorAll('[data-table-search]').forEach(input => {
        input.addEventListener('input', () => {
            const tableId = input.dataset.tableSearch;
            const table = document.getElementById(tableId);
            if(!table) return;
            const q = input.value.trim().toLowerCase();
            table.querySelectorAll('tbody tr').forEach(row => {
                row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
            });
        });
    });

    const passwordToggle = document.getElementById('passwordToggle');
    const password = document.getElementById('password');
    if(passwordToggle && password){
        passwordToggle.addEventListener('click', () => {
            password.type = password.type === 'password' ? 'text' : 'password';
        });
    }

    function addText(parent, tag, text, className){
        const el = document.createElement(tag);
        if(className) el.className = className;
        el.textContent = text == null ? '' : String(text);
        parent.appendChild(el);
        return el;
    }

    function renderLiveAlerts(alerts){
        const list = document.getElementById('liveAlertList');
        if(!list) return;
        list.replaceChildren();
        if(!Array.isArray(alerts) || alerts.length === 0){
            addText(list, 'div', 'No open alerts.', 'empty-note');
            return;
        }
        alerts.forEach(alert => {
            const item = document.createElement('div');
            item.className = 'alert-item';

            const icon = document.createElement('div');
            icon.className = 'alert-icon ' + (alert.severity === 'HIGH' ? 'red' : 'blue');
            icon.textContent = '!';
            item.appendChild(icon);

            const copy = document.createElement('div');
            copy.className = 'alert-copy';
            addText(copy, 'strong', alert.title || 'System alert');
            addText(copy, 'p', alert.message || '');

            const meta = document.createElement('div');
            meta.className = 'alert-meta';
            addText(meta, 'span', alert.severity || 'INFO');
            addText(meta, 'span', alert.category || 'SYSTEM');
            const reference = alert.referenceType
                ? alert.referenceType + (alert.referenceId ? ' #' + alert.referenceId : '')
                : 'SYSTEM';
            addText(meta, 'span', reference);
            copy.appendChild(meta);
            item.appendChild(copy);
            list.appendChild(item);
        });
    }

    const alertFeed = document.querySelector('[data-alert-feed]');
    const contextPath = document.body.dataset.contextPath || (sidebar ? sidebar.dataset.contextPath : '') || '';
    const globalAlertBadge = document.getElementById('globalAlertBadge');
    let previousAlertCount = null;
    if(contextPath && (alertFeed || globalAlertBadge)){
        const refreshAlerts = () => {
            fetch(contextPath + '/api/alerts?limit=100', {
                headers: {'Accept': 'application/json'},
                credentials: 'same-origin',
                cache: 'no-store'
            })
            .then(response => response.ok ? response.json() : Promise.reject())
            .then(alerts => {
                if(globalAlertBadge){
                    const count = Array.isArray(alerts) ? alerts.length : 0;
                    globalAlertBadge.textContent = count >= 100 ? '99+' : String(count);
                    if(previousAlertCount !== null && count > previousAlertCount && alerts[0]){
                        if(typeof window.showToast === 'function'){
                            window.showToast('New alert: ' + (alerts[0].title || 'System alert'));
                        }
                    }
                    previousAlertCount = count;
                }
                if(alertFeed){
                    renderLiveAlerts(Array.isArray(alerts) ? alerts.slice(0, 5) : []);
                }
            })
            .catch(() => {});
        };
        refreshAlerts();
        window.setInterval(refreshAlerts, 2000);
    }

    window.showToast = function(message){
        let toast = document.getElementById('toast');
        if(!toast){
            toast = document.createElement('div');
            toast.className = 'toast';
            toast.id = 'toast';
            document.body.appendChild(toast);
        }
        toast.textContent = message;
        toast.classList.add('show');
        clearTimeout(window.__toastTimer);
        window.__toastTimer = setTimeout(() => toast.classList.remove('show'), 2600);
    };
})();
