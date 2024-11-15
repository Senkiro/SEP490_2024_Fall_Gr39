import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/login',
        name: 'LoginPage',
        component: () => import('../view/auth/login-page.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    {
        path: '/forgot-password',
        name: 'ForgotPasswordPage',
        component: () => import('../view/auth/forgot-password.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    //dashboard
    {
        path: '/staff',
        name: 'StaffHomePage',
        component: () => import('../view/dashboard/staff-homepage.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/manager',
        name: 'ManagerHomePage',
        component: () => import('../view/dashboard/manager-homepage.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    //staff router
    {
        path: '/staff/batch-record',
        name: 'BatchRecord',
        component: () => import('../view/staff/batch-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/batch-detail/:batchName',
        name: 'BatchDetail',
        component: () => import('../view/staff/batch-detail.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/class-record',
        name: 'ClassRecord',
        component: () => import('../view/staff/class-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/student-record',
        name: 'StudentRecord',
        component: () => import('../view/staff/student-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/teacher-record',
        name: 'TeacherRecord',
        component: () => import('../view/staff/teacher-record.vue'),
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
        component: () => import('../view/staff/import-student.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/schedule',
        name: 'Schedule',
        component: () => import('../view/staff/schedule-management.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/time-slot',
        name: 'TimeSlot',
        component: () => import('../view/staff/timeslot-list.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/event',
        name: 'Event',
        component: () => import('../view/staff/event-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/event-detail',
        name: 'EventDetail',
        component: () => import('../view/staff/event-detail.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/room',
        name: 'Room',
        component: () => import('../view/staff/room-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/lesson',
        name: 'LessonList',
        component: () => import('../view/staff/lesson-list.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/lesson-detail',
        name: 'LessonDetail',
        component: () => import('../view/staff/lesson-detail.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/exam',
        name: 'ExamList',
        component: () => import('../view/staff/exam-list.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/exam-detail',
        name: 'ExamDetail',
        component: () => import('../view/staff/exam-detail.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/news',
        name: 'News',
        component: () => import('../view/staff/news-record.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/mark',
        name: 'Mark',
        component: () => import('../view/staff/mark_report.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/create-news',
        name: 'CreateNews',
        component: () => import('../view/staff/create_news.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },
    {
        path: '/staff/student-mark-report',
        name: 'StudentMarkReport',
        component: () => import('../view/staff/student-mark-report.vue'),
        meta: { layout: 'app', requiresAuth: true },
    },

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

        next();
    }
});

export default router;
