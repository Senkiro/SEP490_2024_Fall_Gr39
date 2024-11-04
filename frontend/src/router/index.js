import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/login',
        name: 'LoginPage',
        component: () => import('../view/auth/loginPage.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    {
        path: '/forgot-password',
        name: 'ForgotPasswordPage',
        component: () => import('../view/auth/forgotPasswordPage.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    //dashboard
    {
        path: '/staff',
        name: 'StaffHomePage',
        component: () => import('../view/dashboard/staffHomepage.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/manager',
        name: 'ManagerHomePage',
        component: () => import('../view/dashboard/managerHomepage.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    //staff router
    {
        path: '/staff/batch-record',
        name: 'BatchRecord',
        component: () => import('../view/staff/batch_record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/batch-detail',
        name: 'BatchDetail',
        component: () => import('../view/staff/batch_detail.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/class-record',
        name: 'ClassRecord',
        component: () => import('../view/staff/class-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/teacher-record',
        name: 'TeacherRecord',
        component: () => import('../view/staff/teacher-reacord.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/student-profile/:id',
        name: 'StudentProfile',
        component: () => import('../view/staff/student-profile.vue'),
        meta: { layout: 'app', requiresAuth: true },
        props: true
    },
    {
        path: '/staff/import-student',
        name: 'ImportStudentPage',
        component: () => import('../view/staff/import_student.vue'),
        meta: { layout: 'app', requiresAuth: true },
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// Middleware kiểm tra trạng thái xác thực trước mỗi lần chuyển trang
router.beforeEach((to, from, next) => {
    console.log('Navigating to:', to.path);
    const isAuthenticated = !!sessionStorage.getItem('jwtToken'); // Kiểm tra token lưu trữ trong sessionStorage

    if (to.meta.requiresAuth && !isAuthenticated) {
        // Nếu route yêu cầu xác thực và người dùng chưa đăng nhập, chuyển hướng tới trang đăng nhập
        next({ name: 'LoginPage' });
    } else {
        // Nếu người dùng đã đăng nhập hoặc route không yêu cầu xác thực, cho phép truy cập
        next();
    }
});

export default router;
