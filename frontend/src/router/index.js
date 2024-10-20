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
        meta: { layout: 'app', requiresAuth: true, roles: ['STAFF'] },
    },
    {
        path: '/manager',
        name: 'ManagerHomePage',
        component: () => import('../view/dashboard/managerHomepage.vue'),
        meta: { layout: 'app', requiresAuth: true, roles: ['MANAGER'] },
    },
    //staff router
    {
        path: '/staff/batch-record',
        name: 'BatchRecord',
        component: () => import('../view/staff/batch_record.vue'),
        meta: { layout: 'app', requiresAuth: true, roles: ['STAFF'] },
    },
    {
        path: '/staff/batch-detail',
        name: 'BatchDetail',
        component: () => import('../view/staff/batch_detail.vue'),
        meta: { layout: 'app', requiresAuth: true, roles: ['STAFF'] },
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const token = sessionStorage.getItem('jwtToken');
    const userRole = sessionStorage.getItem('userRole');

    console.log('Navigating to:', to.path);
    console.log('User Role:', userRole);

    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!token) {
            next({ name: 'LoginPage' });
        } else {
            if (to.meta.roles && !to.meta.roles.includes(userRole)) {
                console.warn('Access denied for role:', userRole);
                next({ name: 'LoginPage' });
            } else {
                next();
            }
        }
    } else {
        next();
    }
});

export default router;
