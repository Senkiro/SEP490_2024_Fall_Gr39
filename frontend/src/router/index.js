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
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"}
            ] },
    },
    {
        path: '/manager',
        name: 'ManagerHomePage',
        component: () => import('../view/dashboard/manager-homepage.vue'),
        meta: { 
            layout: 'app', 
            requiresAuth: true,
            breadcrumbs: [
                {name: "Homepage", link:"/manager"},
            ]
         },
    },
    {
        path: '/student',
        name: 'StudentHomepage',
        component: () => import('../view/dashboard/student-homepage.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
            ]
         },
    },
    //staff router
    {
        path: '/staff/batch-record',
        name: 'BatchRecord',
        component: () => import('../view/staff/batch-record.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: ""}
            ]},
    },
    {
        path: '/staff/batch-detail/:batchName',
        name: 'BatchDetail',
        component: () => import('../view/staff/batch-detail.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: "/staff/batch-record"},
                {name: "Batch detail", link: ""}
                
            ]},
    },
    {
        path: '/staff/class-record',
        name: 'ClassRecord',
        component: () => import('../view/staff/class-record.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Class record", link: ""}
            ] },
    },
    {
        path: '/staff/student-record',
        name: 'StudentRecord',
        component: () => import('../view/staff/student-record.vue'),
        meta: { layout: 'app', requiresAuth: true , 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Student record", link: ""}
            ]},
    },
    {
        path: '/staff/teacher-record',
        name: 'TeacherRecord',
        component: () => import('../view/staff/teacher-record.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Teacher record", link: ""}
            ] },
    },
    {
        path: '/staff/student-profile/:id',
        name: 'StudentProfile',
        component: () => import('../view/staff/student-profile.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: "/staff/batch-record"},
                {name: "Batch detail", link: "/staff/batch-detail"},
                {name: "Student profile", link: ""}
            ] },
        props: true
    },
    {
        path: '/staff/import-student',
        name: 'ImportStudentPage',
        component: () => import('../view/staff/import-student.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: "/staff/batch-record"},
                {name: "Batch detail", link: "/staff/batch-detail"},
                {name: "Import students", link: ""}
            ]
        },
    },
    {
        path: '/staff/schedule',
        name: 'Schedule',
        component: () => import('../view/staff/schedule-management.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Schedule management", link: ""}
            ]
        },
    },
    {
        path: '/staff/time-slot',
        name: 'TimeSlot',
        component: () => import('../view/staff/timeslot-list.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Time slot list", link: ""}
            ]
        },
    },
    {
        path: '/staff/event',
        name: 'Event',
        component: () => import('../view/staff/event-record.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Event list", link: ""}
            ]
        },
    },
    {
        path: '/staff/event-detail/:eventId',
        name: 'EventDetail',
        component: () => import('../view/staff/event-detail.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Event list", link: "/staff/event"},
                {name: "Event detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/room',
        name: 'Room',
        component: () => import('../view/staff/room-record.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Room list", link: ""}
            ]
        },
    },
    {
        path: '/staff/lesson',
        name: 'LessonList',
        component: () => import('../view/staff/lesson-list.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Lesson list", link: ""}
            ]
        },
    },
    {
        path: '/staff/lesson-detail',
        name: 'LessonDetail',
        component: () => import('../view/staff/lesson-detail.vue'),
        meta: { layout: 'app', requiresAuth: true , 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Lesson list", link: "/staff/lesson"},
                {name: "Lesson detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/exam',
        name: 'ExamList',
        component: () => import('../view/staff/exam-list.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Exam list", link: ""}
            ]
        },
    },
    {
        path: '/staff/exam-detail',
        name: 'ExamDetail',
        component: () => import('../view/staff/exam-detail.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Exam list", link: "/staff/exam"},
                {name: "Exam detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/news',
        name: 'News',
        component: () => import('../view/staff/news-record.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "News list", link: ""}
            ]
        },
    },
    {
        path: '/staff/mark',
        name: 'Mark',
        component: () => import('../view/staff/mark_report.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Mark report", link: ""}
            ]
        },
    },
    {
        path: '/staff/create-news',
        name: 'CreateNews',
        component: () => import('../view/staff/create_news.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "News list", link: "/staff/news"},
                {name: "Create news", link: ""}
            ]
        },
    },
    {
        path: '/staff/student-mark-report',
        name: 'StudentMarkReport',
        component: () => import('../view/staff/student-mark-report.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Mark report", link: "/staff/mark"},
                {name: "Student's mark report", link: ""}
            ]
        },
    },
    {
        path: '/staff/attendance',
        name: 'AttendanceReport',
        component: () => import('../view/staff/attendance-report.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Attendance report", link: ""}
            ]
        },
    },
    {
        path: '/staff/student-attendance-report',
        name: 'StudentAttendanceReport',
        component: () => import('../view/staff/student-attendance-report.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Attendance report", link: "/staff/attendance"},
                {name: "Student's attendance report", link: ""}
            ]
        },
    },
    //---------------student router---------------
    {
        path: '/student/schedule',
        name: 'StudentSchedule',
        component: () => import('../view/student/schedule-view.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Schedule", link: ""}
            ]
        },
    },
    {
        path: '/student/lesson',
        name: 'StudentLessonList',
        component: () => import('../view/student/lesson-list.vue'),
        meta: { layout: 'app', requiresAuth: true, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Lesson list", link: ""}
            ]
        },
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
