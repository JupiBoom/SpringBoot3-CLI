// API基础URL
const API_BASE_URL = '/api';

// 当前页面和分页状态
let currentPage = 1;
let pageSize = 10;
let currentLiveRoomId = null;

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    loadLiveRooms();
    loadLiveRoomOptions();
});

// 显示指定部分
function showSection(sectionId) {
    // 隐藏所有部分
    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.style.display = 'none';
    });
    
    // 显示指定部分
    document.getElementById(sectionId + '-section').style.display = 'block';
    
    // 更新导航栏活动状态
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.classList.remove('active');
    });
    event.target.classList.add('active');
    
    // 根据不同部分加载相应数据
    switch(sectionId) {
        case 'live-rooms':
            loadLiveRooms();
            break;
        case 'products':
            loadLiveRoomOptions();
            break;
        case 'stats':
            loadLiveRoomOptions('stats-live-room-select');
            break;
        case 'analytics':
            loadLiveRoomOptions('analytics-live-room-select');
            break;
    }
}

// 加载直播间列表
async function loadLiveRooms(page = 1) {
    try {
        const response = await fetch(`${API_BASE_URL}/live-room/page?current=${page}&size=${pageSize}`);
        const result = await response.json();
        
        if (result.success) {
            const liveRooms = result.data.records;
            const tbody = document.getElementById('live-rooms-tbody');
            tbody.innerHTML = '';
            
            liveRooms.forEach(room => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${room.id}</td>
                    <td>${room.title}</td>
                    <td>${room.hostId}</td>
                    <td>
                        <span class="badge ${getStatusBadgeClass(room.status)} status-badge">
                            ${getStatusText(room.status)}
                        </span>
                    </td>
                    <td>${room.viewerCount || 0}</td>
                    <td>${formatDateTime(room.startTime)}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="viewLiveRoom(${room.id})">
                            <i class="bi bi-eye"></i> 查看
                        </button>
                        <button class="btn btn-sm btn-outline-warning" onclick="editLiveRoom(${room.id})">
                            <i class="bi bi-pencil"></i> 编辑
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteLiveRoom(${room.id})">
                            <i class="bi bi-trash"></i> 删除
                        </button>
                    </td>
                `;
                tbody.appendChild(row);
            });
            
            // 更新分页
            updatePagination('live-rooms-pagination', result.data.current, result.data.pages, loadLiveRooms);
            currentPage = result.data.current;
        }
    } catch (error) {
        console.error('加载直播间列表失败:', error);
        alert('加载直播间列表失败，请稍后重试');
    }
}

// 加载直播间选项
async function loadLiveRoomOptions(selectId = 'live-room-select') {
    try {
        const response = await fetch(`${API_BASE_URL}/live-room/list`);
        const result = await response.json();
        
        if (result.success) {
            const liveRooms = result.data;
            const select = document.getElementById(selectId);
            select.innerHTML = '<option value="">选择直播间</option>';
            
            liveRooms.forEach(room => {
                const option = document.createElement('option');
                option.value = room.id;
                option.textContent = room.title;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error('加载直播间选项失败:', error);
    }
}

// 加载商品列表
async function loadProducts() {
    const liveRoomId = document.getElementById('live-room-select').value;
    if (!liveRoomId) {
        document.getElementById('products-container').innerHTML = '<p>请选择直播间</p>';
        return;
    }
    
    currentLiveRoomId = liveRoomId;
    
    try {
        const response = await fetch(`${API_BASE_URL}/live-room-product/list/${liveRoomId}`);
        const result = await response.json();
        
        if (result.success) {
            const products = result.data;
            const container = document.getElementById('products-container');
            container.innerHTML = '';
            
            products.forEach(product => {
                const col = document.createElement('div');
                col.className = 'col-md-4 mb-4';
                col.innerHTML = `
                    <div class="card product-card ${product.isCurrent ? 'current-product' : ''}">
                        <img src="${product.productImage || 'https://via.placeholder.com/300x200'}" class="card-img-top" alt="${product.productName}">
                        <div class="card-body">
                            <h5 class="card-title">${product.productName}</h5>
                            <p class="card-text text-danger">¥${product.productPrice}</p>
                            <p class="card-text">${product.sellingPoints || '暂无卖点'}</p>
                            <div class="d-flex justify-content-between">
                                <button class="btn btn-sm ${product.isCurrent ? 'btn-warning' : 'btn-outline-primary'}" 
                                        onclick="toggleCurrentProduct(${product.id})">
                                    ${product.isCurrent ? '当前讲解中' : '设为当前讲解'}
                                </button>
                                <button class="btn btn-sm btn-outline-danger" onclick="removeProduct(${product.id})">
                                    移除
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                container.appendChild(col);
            });
            
            // 加载销售排行榜
            loadSalesRanking(liveRoomId);
        }
    } catch (error) {
        console.error('加载商品列表失败:', error);
        alert('加载商品列表失败，请稍后重试');
    }
}

// 加载销售排行榜
async function loadSalesRanking(liveRoomId) {
    try {
        const response = await fetch(`${API_BASE_URL}/live-room-product/sales-ranking/${liveRoomId}`);
        const result = await response.json();
        
        if (result.success) {
            const ranking = result.data;
            const tbody = document.getElementById('sales-ranking-tbody');
            tbody.innerHTML = '';
            
            ranking.forEach((item, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${item.productName}</td>
                    <td>¥${item.productPrice}</td>
                    <td>${item.salesCount}</td>
                    <td>¥${item.salesAmount}</td>
                `;
                tbody.appendChild(row);
            });
        }
    } catch (error) {
        console.error('加载销售排行榜失败:', error);
    }
}

// 加载统计数据
async function loadStats() {
    const liveRoomId = document.getElementById('stats-live-room-select').value;
    if (!liveRoomId) {
        return;
    }
    
    try {
        // 获取转化率统计
        const conversionResponse = await fetch(`${API_BASE_URL}/live-room-analytics/conversion-rate/${liveRoomId}`);
        const conversionResult = await conversionResponse.json();
        
        if (conversionResult.success) {
            const stats = conversionResult.data;
            document.getElementById('total-viewers').textContent = stats.totalViewers || 0;
            document.getElementById('total-orders').textContent = stats.totalOrders || 0;
            document.getElementById('total-sales').textContent = `¥${stats.totalSales || '0.00'}`;
            document.getElementById('conversion-rate').textContent = stats.conversionRatePercent || '0.00%';
        }
        
        // 获取历史统计数据
        const historyResponse = await fetch(`${API_BASE_URL}/live-room-stats/list/${liveRoomId}`);
        const historyResult = await historyResponse.json();
        
        if (historyResult.success) {
            const historyData = historyResult.data;
            renderStatsChart(historyData);
        }
    } catch (error) {
        console.error('加载统计数据失败:', error);
        alert('加载统计数据失败，请稍后重试');
    }
}

// 加载数据分析
async function loadAnalytics() {
    const liveRoomId = document.getElementById('analytics-live-room-select').value;
    if (!liveRoomId) {
        return;
    }
    
    try {
        // 获取观众留存曲线
        const retentionResponse = await fetch(`${API_BASE_URL}/live-room-analytics/viewer-retention/${liveRoomId}`);
        const retentionResult = await retentionResponse.json();
        
        if (retentionResult.success) {
            const retentionData = retentionResult.data;
            renderRetentionChart(retentionData);
        }
        
        // 获取转化率分析
        const conversionResponse = await fetch(`${API_BASE_URL}/live-room-analytics/conversion-rate/${liveRoomId}`);
        const conversionResult = await conversionResponse.json();
        
        if (conversionResult.success) {
            const conversionData = conversionResult.data;
            renderConversionChart(conversionData);
        }
    } catch (error) {
        console.error('加载数据分析失败:', error);
        alert('加载数据分析失败，请稍后重试');
    }
}

// 添加直播间
async function addLiveRoom() {
    const title = document.getElementById('live-room-title').value;
    const description = document.getElementById('live-room-description').value;
    const coverImage = document.getElementById('live-room-cover').value;
    const streamUrl = document.getElementById('live-room-stream').value;
    const hostId = document.getElementById('live-room-host').value;
    const startTime = document.getElementById('live-room-start-time').value;
    
    if (!title || !hostId) {
        alert('请填写必要信息');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/live-room/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title,
                description,
                coverImage,
                streamUrl,
                hostId,
                startTime
            })
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert('直播间创建成功');
            bootstrap.Modal.getInstance(document.getElementById('addLiveRoomModal')).hide();
            document.getElementById('add-live-room-form').reset();
            loadLiveRooms();
        } else {
            alert(result.message || '创建失败');
        }
    } catch (error) {
        console.error('创建直播间失败:', error);
        alert('创建直播间失败，请稍后重试');
    }
}

// 添加商品
async function addProduct() {
    const liveRoomId = document.getElementById('live-room-select').value;
    const productId = document.getElementById('product-id').value;
    const productName = document.getElementById('product-name').value;
    const productImage = document.getElementById('product-image').value;
    const productPrice = document.getElementById('product-price').value;
    const sellingPoints = document.getElementById('product-selling-points').value;
    const displayOrder = document.getElementById('product-display-order').value;
    
    if (!liveRoomId || !productId || !productName || !productPrice) {
        alert('请填写必要信息');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/live-room-product/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                liveRoomId,
                productId,
                productName,
                productImage,
                productPrice,
                sellingPoints,
                displayOrder
            })
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert('商品添加成功');
            bootstrap.Modal.getInstance(document.getElementById('addProductModal')).hide();
            document.getElementById('add-product-form').reset();
            loadProducts();
        } else {
            alert(result.message || '添加失败');
        }
    } catch (error) {
        console.error('添加商品失败:', error);
        alert('添加商品失败，请稍后重试');
    }
}

// 切换当前讲解商品
async function toggleCurrentProduct(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/live-room-product/toggle-current/${productId}`, {
            method: 'POST'
        });
        
        const result = await response.json();
        
        if (result.success) {
            loadProducts();
        } else {
            alert(result.message || '操作失败');
        }
    } catch (error) {
        console.error('切换当前讲解商品失败:', error);
        alert('操作失败，请稍后重试');
    }
}

// 移除商品
async function removeProduct(productId) {
    if (!confirm('确定要移除这个商品吗？')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/live-room-product/remove/${productId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.success) {
            loadProducts();
        } else {
            alert(result.message || '移除失败');
        }
    } catch (error) {
        console.error('移除商品失败:', error);
        alert('移除失败，请稍后重试');
    }
}

// 渲染统计图表
function renderStatsChart(data) {
    const ctx = document.getElementById('stats-chart').getContext('2d');
    
    // 销毁已存在的图表实例
    if (window.statsChart) {
        window.statsChart.destroy();
    }
    
    window.statsChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.map(item => item.statsDate),
            datasets: [
                {
                    label: '观众人数',
                    data: data.map(item => item.totalViewers),
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1
                },
                {
                    label: '订单数',
                    data: data.map(item => item.totalOrders),
                    borderColor: 'rgb(255, 99, 132)',
                    tension: 0.1
                },
                {
                    label: '销售额',
                    data: data.map(item => item.totalSales),
                    borderColor: 'rgb(255, 205, 86)',
                    tension: 0.1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// 渲染留存曲线图
function renderRetentionChart(data) {
    const ctx = document.getElementById('retention-chart').getContext('2d');
    
    // 销毁已存在的图表实例
    if (window.retentionChart) {
        window.retentionChart.destroy();
    }
    
    window.retentionChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.retentionPoints.map(item => `${item.timePoint}分钟`),
            datasets: [
                {
                    label: '在线观众人数',
                    data: data.retentionPoints.map(item => item.onlineViewers),
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.1,
                    yAxisID: 'y'
                },
                {
                    label: '留存率 (%)',
                    data: data.retentionPoints.map(item => item.retentionRate),
                    borderColor: 'rgb(255, 99, 132)',
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    tension: 0.1,
                    yAxisID: 'y1'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: 'index',
                intersect: false,
            },
            scales: {
                y: {
                    type: 'linear',
                    display: true,
                    position: 'left',
                    title: {
                        display: true,
                        text: '在线观众人数'
                    }
                },
                y1: {
                    type: 'linear',
                    display: true,
                    position: 'right',
                    title: {
                        display: true,
                        text: '留存率 (%)'
                    },
                    grid: {
                        drawOnChartArea: false,
                    },
                },
            }
        }
    });
}

// 渲染转化率图表
function renderConversionChart(data) {
    const ctx = document.getElementById('conversion-chart').getContext('2d');
    
    // 销毁已存在的图表实例
    if (window.conversionChart) {
        window.conversionChart.destroy();
    }
    
    window.conversionChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['已转化', '未转化'],
            datasets: [{
                data: [data.totalOrders, data.totalViewers - data.totalOrders],
                backgroundColor: [
                    'rgba(75, 192, 192, 0.8)',
                    'rgba(255, 99, 132, 0.8)'
                ],
                borderColor: [
                    'rgb(75, 192, 192)',
                    'rgb(255, 99, 132)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: `转化率: ${data.conversionRatePercent}`
                },
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

// 更新分页
function updatePagination(paginationId, currentPage, totalPages, loadFunction) {
    const pagination = document.getElementById(paginationId);
    pagination.innerHTML = '';
    
    // 上一页
    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    prevLi.innerHTML = `<a class="page-link" href="#" onclick="${loadFunction.name}(${currentPage - 1})">上一页</a>`;
    pagination.appendChild(prevLi);
    
    // 页码
    for (let i = 1; i <= totalPages; i++) {
        if (i === 1 || i === totalPages || (i >= currentPage - 2 && i <= currentPage + 2)) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#" onclick="${loadFunction.name}(${i})">${i}</a>`;
            pagination.appendChild(li);
        } else if (i === currentPage - 3 || i === currentPage + 3) {
            const li = document.createElement('li');
            li.className = 'page-item disabled';
            li.innerHTML = '<a class="page-link" href="#">...</a>';
            pagination.appendChild(li);
        }
    }
    
    // 下一页
    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    nextLi.innerHTML = `<a class="page-link" href="#" onclick="${loadFunction.name}(${currentPage + 1})">下一页</a>`;
    pagination.appendChild(nextLi);
}

// 获取状态徽章类
function getStatusBadgeClass(status) {
    switch (status) {
        case 0: return 'bg-secondary'; // 未开始
        case 1: return 'bg-success';   // 直播中
        case 2: return 'bg-warning';   // 暂停
        case 3: return 'bg-danger';    // 已结束
        default: return 'bg-secondary';
    }
}

// 获取状态文本
function getStatusText(status) {
    switch (status) {
        case 0: return '未开始';
        case 1: return '直播中';
        case 2: return '暂停';
        case 3: return '已结束';
        default: return '未知';
    }
}

// 格式化日期时间
function formatDateTime(dateTime) {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString('zh-CN');
}

// 查看直播间详情
function viewLiveRoom(liveRoomId) {
    // 这里可以实现查看直播间详情的逻辑
    alert(`查看直播间 ${liveRoomId} 的详情`);
}

// 编辑直播间
function editLiveRoom(liveRoomId) {
    // 这里可以实现编辑直播间的逻辑
    alert(`编辑直播间 ${liveRoomId}`);
}

// 删除直播间
async function deleteLiveRoom(liveRoomId) {
    if (!confirm('确定要删除这个直播间吗？')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/live-room/delete/${liveRoomId}`, {
            method: 'DELETE'
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert('删除成功');
            loadLiveRooms();
        } else {
            alert(result.message || '删除失败');
        }
    } catch (error) {
        console.error('删除直播间失败:', error);
        alert('删除失败，请稍后重试');
    }
}