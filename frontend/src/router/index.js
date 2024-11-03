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
        meta: { layout: 'app', requiresAuth: false },
    },
    {
        path: '/manager',
        name: 'ManagerHomePage',
        component: () => import('../view/dashboard/managerHomepage.vue'),
        meta: { layout: 'app', requiresAuth: false },
    },
    //staff router
    {
        path: '/staff/batch-record',
        name: 'BatchRecord',
        component: () => import('../view/staff/batch_record.vue'),
        meta: { layout: 'app', requiresAuth: false },
    },
    {
        path: '/staff/batch-detail',
        name: 'BatchDetail',
        component: () => import('../view/staff/batch_detail.vue'),
        meta: { layout: 'app', requiresAuth: false },
    },
    {
        path: '/staff/class-record',
        name: 'ClassRecord',
        component: () => import('../view/staff/class-record.vue'),
        meta: { layout: 'app', requiresAuth: false },
    },
    {
        path: '/staff/teacher-record',
        name: 'TeacherRecord',
        component: () => import('../view/staff/teacher-reacord.vue'),
        meta: { layout: 'app', requiresAuth: false },
    },
    {
        path: '/staff/student-profile/:id',
        name: 'StudentProfile',
        component: () => import('../view/staff/student-profile.vue'),
        meta: { layout: 'app', requiresAuth: false },
        props: true
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

// Middleware đơn giản cho phép truy cập tất cả các trang
router.beforeEach((to, from, next) => {
    console.log('Navigating to:', to.path);
    next();
});

export default router;