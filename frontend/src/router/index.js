import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../view/LoginPage.vue';
import UserManagement from '../view/userManagement.vue';

const routes = [
    { path: '/login', name: 'LoginPage', component: LoginPage },
    {
        path: '/user-management',
        name: 'UserManagement',
        component: UserManagement,
        meta: { requiresAuth: true }
    }
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
