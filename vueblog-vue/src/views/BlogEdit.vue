<template>
    <div>
        <Header></Header>
        <div class="m-content">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="ruleForm.title"></el-input>
                </el-form-item>
                <el-form-item label="摘要" prop="description">
                    <el-input type="textarea" v-model="ruleForm.description"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <mavon-editor v-model="ruleForm.content"></mavon-editor>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    import Header from "../components/Header";
    export default {
        name: "BlogEdit.vue",
        components: {Header},
        data() {
            return {
                ruleForm: {
                    id: '',
                    title: '',
                    description: '',
                    content: ''
                },
                rules: {
                    title: [
                        { required: true, message: '请输入标题', trigger: 'blur' },
                        { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
                    ],
                    description: [
                        { required: true, message: '请输入摘要', trigger: 'change' }
                    ],
                    content: [
                        { required: true, message: '请输入内容', trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$confirm('确认提交?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$refs[formName].validate((valid) => {
                        if (valid) {
                            this.$axios.post("/blog/edit", this.ruleForm, {
                                headers: {
                                    "Authorization": localStorage.getItem("token")
                                }
                            }).then(res => {
                                console.log("res:"+res)
                                this.$router.push("/blogs") //路由设在这里跳到首页操作在编辑结果入库之后
                            })
                            this.$message({
                                type: 'success',
                                message: '提交成功!'
                            });
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
                    // this.$message({
                    //     type: 'success',
                    //     message: '提交成功!'
                    // });
                    // this.$router.push("/blogs") //路由设在这里跳到首页操作在编辑结果入库之前
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消提交'
                    });
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        },
        created() {
            const blogId = this.$route.params.blogId
            // console.log(blogId)
            if(blogId){ //编辑
                this.$axios.get("/blog/"+blogId).then(res => {
                    const blog = res.data.data
                    this.ruleForm.id = blog.id
                    this.ruleForm.title = blog.title
                    this.ruleForm.description = blog.description
                    this.ruleForm.content = blog.content

                })
            }
        }
    }
</script>

<style scoped>
    .m-content {
        text-align: center;
    }
</style>