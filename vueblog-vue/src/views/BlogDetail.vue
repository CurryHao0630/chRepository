<template>
    <div>
        <Header></Header>
        <div class="mblog">
            <h2>
                {{blog.title}}
<!--                <el-link icon="el-icon-edit" v-show="hasAutho">-->
<!--                    <router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}">-->
<!--                        编辑-->
<!--                    </router-link>-->
<!--                </el-link>-->
            </h2>
            <el-button-group>
                <el-button type="primary" icon="el-icon-edit" v-show="hasAutho" @click="toEdit"></el-button>
                <el-button type="danger" icon="el-icon-delete" v-show="hasAutho" @click="toBlogs"></el-button>
            </el-button-group>
            <!--横向分割线-->
            <el-divider></el-divider>
            <div class="markdown-body" v-html="blog.content"></div>
        </div>
    </div>
</template>

<script>
    import Header from "../components/Header";
    import "github-markdown-css/github-markdown.css"
    export default {
        name: "BlogDetail.vue",
        components: {Header},
        data() {
            return {
                blog: {
                    id: "",
                    title: "",
                    content: ""
                },
                hasAutho: false
            }
        },
        methods: {
            toEdit() {
                this.$router.push("/blog/"+this.blog.id+"/edit")
            },
            toBlogs() {
                this.$axios.get("/blog/delete?id="+this.blog.id, {
                    headers: {
                        "Authorization": localStorage.getItem("token")
                    }
                }).then(res => {
                    this.$router.push("/blogs")
                })

            }
        },
        created() {
            const blogId = this.$route.params.blogId
            if(blogId){
                this.$axios.get("/blog/"+blogId).then(res => {
                    const blog = res.data.data
                    this.blog.id = blog.id
                    this.blog.title = blog.title

                    if(blog.userId === this.$store.getters.getUser.id){
                        this.hasAutho = true
                    }


                    // markdown渲染(将后台传来的数据以html形式展现)
                    var MarkdownIt = require("markdown-it")
                    var md = new MarkdownIt()
                    var result = md.render(blog.content)
                    this.blog.content = result

                })
            }
        }
    }
</script>

<style scoped>
    .mblog {
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        width: 100%;
        min-height: 666px;
    }
</style>