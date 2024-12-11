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
    //admin
    {
        path: '/admin/account',
        name: 'AdminAccount',
        component: () => import('../view/admin/account-record.vue'),
        meta: { 
            layout: 'admin', 
            requiresAuth: false, 
        }, 
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
        name: 'StaffStudentProfile',
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
        path: '/staff/import-holiday',
        name: 'ImportHoliday',
        component: () => import('../view/staff/import-holiday.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Holiday list", link: "/staff/holiday"},
                {name: "Import holiday", link: ""}
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
        path: '/staff/exam-type',
        name: 'StaffExamType',
        component: () => import('../view/staff/exam-type-rate.vue'),
        meta: { layout: 'staff', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Event type", link: ""}
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
        path: '/staff/lesson-detail/:id',
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
        path: '/staff/exam-detail/:id',
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
        component: () => import('../view/staff/news-create.vue'),
        meta: { layout: 'staff', requiresAuth: false,
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "News list", link: "/staff/news"},
                {name: "Create news", link: ""}
            ]
        },
    },
    {
        path: '/staff/holiday',
        name: 'HolidayList',
        component: () => import('../view/staff/holiday-list.vue'),
        meta: { layout: 'staff', requiresAuth: false,
            breadcrumbs: [
                {name: "Homepage", link:"/staff"},
                {name: "Holiday list", link: ""}
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
        path: '/staff/student-attendance-report/:id',
        name: 'StaffStudentAttendanceReport',
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
        path: '/student/teacher-record',
        name: 'StudentTeacherRecord',
        component: () => import('../view/student/teacher-record.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Teacher record", link:""},
            ]},
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
        path: '/student/event-detail/:id',
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
        name: 'StudentStudentAttendanceReport',
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
        path: '/student/news-detail/:id',
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
    {
        path: '/student/holiday',
        name: 'StudentHolidayList',
        component: () => import('../view/student/holiday-list.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Holiday list", link: ""}
            ]
        },
    },
    {
        path: '/student/student-profile/:id',
        name: 'MyStudentProfile',
        component: () => import('../view/staff/student-profile.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Student profile", link: ""}
            ] },
        props: false
    },
    {
        path: '/student/student-profile/:id',
        name: 'StudentStudentProfile',
        component: () => import('../view/teacher/student-profile.vue'),
        meta: { layout: 'student', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/student"},
                {name: "Class member", link: "/student/class"},
                {name: "Student profile", link: ""}
            ] },
        props: false
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
        path: '/teacher/class-record',
        name: 'TeacherClassRecord',
        component: () => import('../view/teacher/class-record.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Class record", link:""},
            ]},
    },
    {
        path: '/teacher/teacher-record',
        name: 'TeacherTeacherRecord',
        component: () => import('../view/student/teacher-record.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Teacher record", link:""},
            ]},
    },
    {
        path: '/teacher/student-record',
        name: 'TeacherStudentRecord',
        component: () => import('../view/teacher/student-record.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Class record", link:"/teacher/class"},
                {name: "Student record", link: ""}
            ]},
    },
    {
        path: '/teacher/student-profile/:id',
        name: 'TeacherStudentProfile',
        component: () => import('../view/teacher/student-profile.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Class record", link: "/teacher/class"},
                {name: "Student profile", link: ""}
            ] },
        props: false
    },
    {
        path: '/teacher/curriculumn',
        name: 'TeacherCurriculumnDetail',
        component: () => import('../view/student/curriculumn-detail.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Curriculumn", link: ""}
            ]},
    },
    {
        path: '/teacher/holiday',
        name: 'TeacherHolidayList',
        component: () => import('../view/student/holiday-list.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Holiday list", link: ""}
            ]
        },
    },
    {
        path: '/teacher/lesson-detail',
        name: 'TeacherLessonDetail',
        component: () => import('../view/student/lesson-detail.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Curriculumn", link:"/teacher/curriculumn"},
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
                {name: "Curriculumn", link:"/teacher/curriculumn"},
                {name: "Exam detail", link: ""}
            ]},
    },
    {
        path: '/teacher/mark',
        name: 'TeacherMarkEntryManagement',
        component: () => import('../view/teacher/mark-entry-management.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Mark entry management", link: ""}
            ]},
    },
    {
        path: '/teacher/mark-management',
        name: 'TeacherMarkManagement',
        component: () => import('../view/teacher/mark-management.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Mark entry management", link:"/teacher/mark"},
                {name: "Mark management", link: ""}
            ]},
    },
    {
        path: '/teacher/event',
        name: 'TeacherEventList',
        component: () => import('../view/student/event-list.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Event list", link: ""}
            ]},
    },
    {
        path: '/teacher/event-detail',
        name: 'TeacherEventDetail',
        component: () => import('../view/student/event-detail.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Event list", link:"/teacher/event"},
                {name: "Event detail", link: ""}
            ]
        },
    },
    {
        path: '/teacher/attendance-management',
        name: 'TeacherAttendanceManagement',
        component: () => import('../view/teacher/attendance-management.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Attendance management", link: ""}
            ]
        },
    },
    {
        path: '/teacher/take-attendance',
        name: 'TeacherTakeAttendance',
        component: () => import('../view/teacher/take-attendance.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "Attendance management", link: "/teacher/attendance-management"},
                {name: "Take attendance", link: ""}
            ]
        },
    },
    {
        path: '/teacher/news',
        name: 'TeacherNewsList',
        component: () => import('../view/student/news-list.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "News list", link: ""}
            ]
        },
    },
    {
        path: '/teacher/news-detail',
        name: 'TeacherNewsDetail',
        component: () => import('../view/student/news-detail.vue'),
        meta: { layout: 'teacher', requiresAuth: false, 
            breadcrumbs: [
                {name: "Homepage", link:"/teacher"},
                {name: "News list", link: "/teacher/news"},
                {name: "News detail", link: ""}
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
