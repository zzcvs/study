function startCooking(orderId) {
    confirmAction('确定开始制作此订单吗？', function() {
        const formData = new FormData();
        formData.append('orderId', orderId);
        
        fetch('/kitchen/order/startCooking', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                showNotification('订单开始制作', 'success');
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

function completeOrder(orderId) {
    confirmAction('确定完成此订单吗？', function() {
        const formData = new FormData();
        formData.append('orderId', orderId);
        
        fetch('/kitchen/order/complete', {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            if (data === 'success') {
                showNotification('订单制作完成', 'success');
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
