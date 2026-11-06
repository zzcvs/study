function createOrder() {
    const selectedItems = [];
    const menuItems = document.querySelectorAll('.menu-item');
    
    menuItems.forEach(item => {
        const checkbox = item.querySelector('input[type="checkbox"]');
        const quantityInput = item.querySelector('input[type="number"]');
        
        if (checkbox && checkbox.checked && quantityInput) {
            const quantity = parseInt(quantityInput.value) || 0;
            if (quantity > 0) {
                selectedItems.push({
                    recipeId: checkbox.value,
                    quantity: quantity
                });
            }
        }
    });
    
    if (selectedItems.length === 0) {
        showNotification('请选择菜品', 'error');
        return;
    }
    
    const formData = new FormData();
    const mealTypeField = document.getElementById('orderMealType');
    const dateField = document.getElementById('orderDate');
    formData.append('mealType', mealTypeField ? mealTypeField.value : document.querySelector('select[name="mealType"]').value);
    if (dateField) {
        formData.append('date', dateField.value);
    }
    formData.append('remark', document.querySelector('textarea[name="remark"]').value);
    
    const recipeIds = selectedItems.map(item => item.recipeId).join(',');
    const quantities = selectedItems.map(item => item.quantity).join(',');
    
    formData.append('recipeIds', recipeIds);
    formData.append('quantities', quantities);
    
    fetch('/employee/order/create', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showNotification(`订单创建成功！订单号：${data.orderNo}`, 'success');
            setTimeout(() => {
                window.location.href = '/employee/orders';
            }, 2000);
        } else {
            showNotification(data.message || '订单创建失败', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification('订单创建失败，请稍后重试', 'error');
    });
}

function cancelOrder(orderId) {
    confirmAction('确定要取消此订单吗？', function() {
        const formData = new FormData();
        formData.append('orderId', orderId);
        
        fetch('/employee/order/cancel', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showNotification('订单已取消', 'success');
                setTimeout(() => {
                    location.reload();
                }, 1000);
            } else {
                showNotification(data.message || '取消订单失败', 'error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('取消订单失败，请稍后重试', 'error');
        });
    });
}

function viewOrderDetail(orderId) {
    window.location.href = '/employee/order/detail?orderId=' + orderId;
}

function updateTotalAmount() {
    let total = 0;
    const menuItems = document.querySelectorAll('.menu-item');
    
    menuItems.forEach(item => {
        const checkbox = item.querySelector('input[type="checkbox"]');
        const priceSpan = item.querySelector('.price');
        const quantityInput = item.querySelector('input[type="number"]');
        
        if (checkbox && checkbox.checked && priceSpan && quantityInput) {
            const price = parseFloat(priceSpan.textContent.replace('¥', ''));
            const quantity = parseInt(quantityInput.value) || 0;
            total += price * quantity;
        }
    });
    
    const totalElement = document.getElementById('totalAmount');
    if (totalElement) {
        totalElement.textContent = '¥' + total.toFixed(2);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"]');
    const quantityInputs = document.querySelectorAll('input[type="number"]');
    
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateTotalAmount);
    });
    
    quantityInputs.forEach(input => {
        input.addEventListener('change', updateTotalAmount);
        input.addEventListener('input', updateTotalAmount);
    });
});
