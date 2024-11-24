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
    {
        path: '/user-profile/:id',
        name: 'UserProfile',
        component: () => import('../view/auth/view-profile.vue'),
        meta: { layout: 'auth', requiresAuth: false },
    },
    //dashboard
    {
        path: '/staff',
        name: 'StaffHomePage',
        component: () => import('../view/dashboard/staff-homepage.vue'),
        meta: { 
            layout: 'staff', 
            requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"}
            ] }, 
    },
    {
        path: '/manager',
        name: 'ManagerHomePage',
        component: () => import('../view/dashboard/manager-homepage.vue'),
        meta: { 
            layout: 'staff', 
            requiresAuth: false,
            breadcrumbs: [
                {name: "Homepage", link:"/manager"},
            ]
         },
    },
    {
        path: '/teacher',
        name: 'TeacherHomepage',
        component: () => import('../view/dashboard/teacher-homepage.vue'),
        meta: { 
            layout: 'teacher', 
            requiresAuth: false,
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
            ]
         },
    },
    {
        path: '/student',
        name: 'StudentHomepage',
        component: () => import('../view/dashboard/student-homepage.vue'),
        meta: { 
            layout: 'student', 
            requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
            ]
         },
    },
    //------------------------------Staff router------------------------
    {
        path: '/staff/batch-record',
        name: 'BatchRecord',
        component: () => import('../view/staff/batch-record.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: ""}
            ]},
    },
    {
        path: '/staff/batch-detail/:batchName',
        name: 'BatchDetail',
        component: () => import('../view/staff/batch-detail.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
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
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Class record", link: ""}
            ] },
    },
    {
        path: '/staff/student-record',
        name: 'StudentRecord',
        component: () => import('../view/staff/student-record.vue'),
        meta: { layout: 'staff', requiresAuth: false , 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Student record", link: ""}
            ]},
    },
    {
        path: '/staff/teacher-record',
        name: 'TeacherRecord',
        component: () => import('../view/staff/teacher-record.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Teacher record", link: ""}
            ] },
    },
    {
        path: '/staff/student-profile/:id',
        name: 'StudentProfile',
        component: () => import('../view/staff/student-profile.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: "/staff/batch-record"},
                {name: "Batch detail", link: "/staff/batch-detail"},
                {name: "Student profile", link: ""}
            ] },
        props: false
    },
    {
        path: '/staff/import-student',
        name: 'ImportStudent',
        component: () => import('../view/staff/import-student.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Batch record", link: "/staff/batch-record"},
                {name: "Batch detail", link: "/staff/batch-detail"},
                {name: "Import students", link: ""}
            ]
        },
    },
    {
        path: '/staff/import-curriculumn',
        name: 'ImportCurriculumn',
        component: () => import('../view/staff/import-curriculumn.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Curriculumn list", link: "/staff/curriculumn"},
                {name: "Import curriculumn", link: ""}
            ]
        },
    },
    {
        path: '/staff/schedule',
        name: 'StaffSchedule',
        component: () => import('../view/staff/schedule-management.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Schedule management", link: ""}
            ]
        },
    },
    {
        path: '/staff/time-slot',
        name: 'TimeSlotList',
        component: () => import('../view/staff/timeslot-list.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Time slot list", link: ""}
            ]
        },
    },
    {
        path: '/staff/event',
        name: 'EventList',
        component: () => import('../view/staff/event-list.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Event list", link: ""}
            ]
        },
    },
    {
        path: '/staff/event-detail/:eventId',
        name: 'StaffEventDetail',
        component: () => import('../view/staff/event-detail.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Event list", link: "/staff/event"},
                {name: "Event detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/room',
        name: 'RoomList',
        component: () => import('../view/staff/room-record.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Room list", link: ""}
            ]
        },
    },
    {
        path: '/staff/lesson-detail',
        name: 'StaffLessonDetail',
        component: () => import('../view/staff/lesson-detail.vue'),
        meta: { layout: 'staff', requiresAuth: false , 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Curriculumn list", link: "/staff/curriculumn"},
                {name: "Curriculumn detail", link: "/staff/curriculumn-detail"},
                {name: "Lesson detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/curriculumn',
        name: 'StaffCurriculumnList',
        component: () => import('../view/staff/curriculumn-list.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Curriculumn list", link: ""}
            ]
        },
    },
    {
        path: '/staff/curriculumn-detail/:id',
        name: 'StaffCurriculumnDetail',
        component: () => import('../view/staff/curriculumn-detail.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Curriculumn list", link:"/staff/curriculumn"},
                {name: "Curriculumn detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/exam-detail',
        name: 'StaffExamDetail',
        component: () => import('../view/staff/exam-detail.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Curriculumn list", link: "/staff/curriculumn"},
                {name: "Curriculumn detail", link: "/staff/curriculumn-detail"},
                {name: "Exam detail", link: ""}
            ]
        },
    },
    {
        path: '/staff/news',
        name: 'StaffNews',
        component: () => import('../view/staff/news-list.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "News list", link: ""}
            ]
        },
    },
    {
        path: "/staff/news-detail/:id",
        name: "StaffNewsDetail",
        component: () => import("../view/staff/news-detail.vue"),
        meta: {layout: "staff", requiresAuth: false,
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "News list", link:"/staff/news"},
                {name: "News detail", link: ""}
            ]
        },
    },

    {
        path: '/staff/mark',
        name: 'StaffMarkReport',
        component: () => import('../view/staff/mark_report.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Mark report", link: ""}
            ]
        },
    },
    {
        path: '/staff/create-news',
        name: 'CreateNews',
        component: () => import('../view/staff/create-news.vue'),
        meta: { layout: 'staff', requiresAuth: false,
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "News list", link: "/staff/news"},
                {name: "Create news", link: ""}
            ]
        },
    },
    {
        path: '/staff/student-mark-report',
        name: 'StaffStudentMarkReport',
        component: () => import('../view/staff/student-mark-report.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
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
        meta: { layout: 'staff', requiresAuth: false, 
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
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Attendance report", link: "/staff/attendance"},
                {name: "Student's attendance report", link: ""}
            ]
        },
    },
    //---------------Student router---------------
    {
        path: '/student/schedule',
        name: 'StudentSchedule',
        component: () => import('../view/student/student-schedule.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Schedule", link: ""}
            ]
        },
    },
    {
        path: '/student/class',
        name: 'ClassMember',
        component: () => import('../view/student/class-member.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Class member", link: ""}
            ]
        },
    },
    {
        path: '/student/curriculumn',
        name: 'StudentCurriculumnDetail',
        component: () => import('../view/student/curriculumn-detail.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Curriculumn", link: ""}
            ]
        },
    },
    {
        path: '/student/lesson-detail',
        name: 'StudentLessonDetail',
        component: () => import('../view/student/lesson-detail.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Curriculumn", link:"/student/curriculumn"},
                {name: "Lesson detail", link: ""}
            ]
        },
    },
    {
        path: '/student/exam-detail',
        name: 'StudentExamDetail',
        component: () => import('../view/student/exam-detail.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Curriculumn", link:"/student/curriculumn"},
                {name: "Exam detail", link: ""}
            ]
        },
    },
    {
        path: '/student/event',
        name: 'StudentEventList',
        component: () => import('../view/student/event-list.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Event list", link: ""}
            ]
        },
    },
    {
        path: '/student/event-detail',
        name: 'StudentEventDetail',
        component: () => import('../view/student/event-detail.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Event list", link:"/student/event"},
                {name: "Event detail", link: ""}
            ]
        },
    },
    {
        path: '/student/mark',
        name: 'StudentMarkReport',
        component: () => import('../view/student/mark-report.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Mark report", link: ""}
            ]
        },
    },
    {
        path: '/student/attendance',
        name: 'StudentAttendanceReport',
        component: () => import('../view/student/attendance-report.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Attendance report", link: ""}
            ]
        },
    },
    {
        path: '/student/news',
        name: 'StudentNewsList',
        component: () => import('../view/student/news-list.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "News list", link: ""}
            ]
        },
    },
    {
        path: '/student/news',
        name: 'StudentNewsDetail',
        component: () => import('../view/student/news-detail.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "News list", link:"/student/news"},
                {name: "News detail", link: ""}
            ]
        },
    },

    //----------------------Teacher router----------------------

    {
        path: '/teacher/schedule',
        name: 'TeacherSchedule',
        component: () => import('../view/teacher/teacher-schedule.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Schedule", link: ""}
            ]},
    },
    {
        path: '/teacher/lesson-detail',
        name: 'TeacherLessonDetail',
        component: () => import('../view/student/lesson-detail.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Lesson list", link:"/teacher/lesson"},
                {name: "Lesson detail", link: ""}
            ]},
    },
    {
        path: '/teacher/exam-detail',
        name: 'TeacherExamDetail',
        component: () => import('../view/student/exam-detail.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Exam list", link:"/teacher/exam"},
                {name: "Exam detail", link: ""}
            ]},
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
