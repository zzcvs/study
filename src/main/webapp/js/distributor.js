function startDistribute(orderId) {
    confirmAction('确定开始配餐吗？', function() {
        const formData = new FormData();
        formData.append('orderId', orderId);
        
        fetch('/distributor/order/startDistribute', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                showNotification('开始配餐', 'success');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification('操作失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('操作失败，请稍后重试', 'error');
        });
    });
}

function completeDistribute(orderId) {
    confirmAction('确定完成配餐吗？', function() {
        const formData = new FormData();
        formData.append('orderId', orderId);
        
        fetch('/distributor/order/completeDistribute', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                showNotification('配餐完成', 'success');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification('操作失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('操作失败，请稍后重试', 'error');
        });
    });
}

function viewOrder(orderId) {
    window.location.href = '/distributor/order/detail?orderId=' + orderId;
}
