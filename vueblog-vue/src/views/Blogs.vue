<template>
    <div>
        <Header></Header>
        <div class="block">
            <el-timeline>
                <el-timeline-item :timestamp="blog.created" placement="top" v-for="blog in blogs">
                    <el-card>
                        <h4>
                            <router-link :to="{name: 'BlogDetail', params: {blogId: blog.id}}">
                                {{blog.title}}
                            </router-link>
                        </h4>
                        <p>{{blog.description}}</p>
                    </el-card>
                </el-timeline-item>
            </el-timeline>
            <el-pagination class="mpage"
                    background
                    layout="prev, pager, next"
                    :current-page="currentPage"
                    :pageSize="pageSize"
                    :total="total"
                    @current-change=page>
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import Header from "../components/Header";
    export default {
        name: "Blogs.vue",
        components: {Header},
        data() {
            return {
                blogs: {},
                currentPage: 1,
                pageSize: 5,
                total: 0
            }
        },
        methods: {
            page(currentPage) {
                const _this = this
                _this.$axios.get("/blogs?currentPage="+currentPage).then(res => {
                    _this.blogs = res.data.data.records
                    _this.currentPage = res.data.data.current
                    _this.pageSize = res.data.data.size
                    _this.total = res.data.data.total
                })
            }
        },
        created() {
            this.page(1)
        }
    }
</script>

<style scoped>
    .mpage {
        text-align: center;
    }
</style>