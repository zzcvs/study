function loadDailyReport(date) {
    const queryDate = date || formatDate(new Date());
    
    fetch(`/finance/daily?date=${queryDate}`)
        .then(response => response.text())
        .then(html => {
            document.querySelector('.content-wrapper').innerHTML = html;
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('加载报表失败', 'error');
        });
}

function loadMonthlyReport(year, month) {
    const queryYear = year || new Date().getFullYear();
    const queryMonth = month || new Date().getMonth() + 1;
    
    fetch(`/finance/monthly?year=${queryYear}&month=${queryMonth}`)
        .then(response => response.text())
        .then(html => {
            document.querySelector('.content-wrapper').innerHTML = html;
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('加载报表失败', 'error');
        });
}

function exportReport(type, params) {
    let url = `/finance/export/${type}`;
    if (params) {
        url += '?' + new URLSearchParams(params).toString();
    }
    
    window.open(url, '_blank');
    showNotification('正在导出报表...', 'success');
}
