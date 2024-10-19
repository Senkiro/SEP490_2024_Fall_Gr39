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
    //staff router
    {
        path: '/staff',
        name: 'StaffHomePage',
        component: () => import('../view/dashboard/staffHomepage.vue'),
        meta: { layout: 'app', requiresAuth: false },
    },
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

];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('jwtToken');
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!token) {
            next({ name: 'Login' });
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
