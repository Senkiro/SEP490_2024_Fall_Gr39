<template>
    <div class="container">
        <div class="headContent">
            <h1>My profile</h1>
        </div>

        <section class="info-card">
            <div class="info-wrapper">
                <img src="../../assets/smiling-young-man-illustration_1308-174669.avif" class="avatar">

                <div class="details-container">
                    <div class="name-id-container">
                        <div class="name-id">
                            <h3><strong>Mai Thế Nam - マイ・テ・ナム</strong></h3>
                            <p>HE123456</p>
                        </div>
                        <button class="btn-edit">
                            <VsxIcon iconName="Edit2" :size="18" color="#495057" type="linear" />Edit
                        </button>
                    </div>

                    <div class="details">
                        <div class="column column1">
                            <div class="attribute">
                                <p>Email</p>
                                <strong>nammthe160123@fpt.edu.vn</strong>
                            </div>
                            <div class="attribute">
                                <p>Gender</p>
                                <strong>Male</strong>
                            </div>
                            <div class="attribute">
                                <p>DOB</p>
                                <strong>22/02/2001</strong>
                            </div>
                        </div>

                        <div class="column column2">
                            <div class="attribute">
                                <p>Phone</p>
                                <strong>0123456789</strong>
                            </div>
                            <div class="attribute">
                                <p>Username</p>
                                <strong>username</strong>
                            </div>
                            <div class="attribute">
                                <p>Password</p>
                                <strong>password</strong>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
.info-wrapper {
    display: flex;
    align-items: flex-start;
    gap: 20px;
    max-height: 400px;

    img {
        height: 290px;
        border-radius: 20px;
    }
}


.details-container {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    border: 1px solid #ddd;
    border-radius: 20px;
    padding: 20px 30px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    gap: 30px;

    .name-id-container {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        padding-bottom: 20px;

        .btn-edit {
            display: flex;
            color: var(--border);
            background: none;
            border: 0.2px solid var(--border);
            border-radius: 5px;
            padding: 3px 8px;
            font-size: 12px;
            height: fit-content;
            align-items: center;
            gap: 5px;
        }
    }

    .details {
        display: flex;
        flex-direction: row;
        gap: 30px;
        font-size: 14px;

        .column3 {
            width: 350px;
        }

        .column {
            display: flex;
            gap: 20px;
            flex-direction: column;

            .attribute {
                display: flex;
                flex-direction: column;
                gap: 5px;

                p {
                    color: #6C757D;
                }
            }
        }
    }
}
</style>