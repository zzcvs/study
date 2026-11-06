function deleteUser(userId) {
    confirmAction('确定要删除此用户吗？', function() {
        fetch(`/manager/user/delete/${userId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                showNotification('用户删除成功', 'success');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification(data.message || '删除失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('删除失败，请稍后重试', 'error');
        });
    });
}

function deleteRecipe(recipeId) {
    confirmAction('确定要删除此菜品吗？', function() {
        fetch(`/manager/recipe/delete/${recipeId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                showNotification('菜品删除成功', 'success');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification(data.message || '删除失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('删除失败，请稍后重试', 'error');
        });
    });
}

function deleteOrder(orderId) {
    confirmAction('确定要删除此订单吗？', function() {
        fetch(`/manager/order/delete/${orderId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                showNotification('订单删除成功', 'success');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification(data.message || '删除失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('删除失败，请稍后重试', 'error');
        });
    });
}

function exportFinanceReport(date) {
    const queryDate = date || formatDate(new Date());
    window.open(`/manager/finance/export?date=${queryDate}`, '_blank');
    showNotification('正在导出报表...', 'success');
}
