import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/login',
        name: 'LoginPage',
        component: () => import('../view/auth/LoginPage.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    {
        path: '/forgot-password',
        name: 'ForgotPasswordPage',
        component: () => import('../view/auth/ForgotPasswordPage.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    {
        path: '/staff',
        name: 'StaffHomePage',
        component: () => import('../view/dashboard/StaffHomepage.vue'),
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
