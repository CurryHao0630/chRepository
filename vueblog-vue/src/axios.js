import axios from 'axios'
import Element from 'element-ui'
import router from './router'
import store from './store'

axios.defaults.baseURL = "http://localhost:8080"

//前置拦截
axios.interceptors.request.use(config => {
    return config
})

//后置拦截
axios.interceptors.response.use(
    //前端请求成功时（200）
    response => {
        let res = response.data

        console.log("=======")
        console.log(res)
        console.log("=======")

        if(res.code === 200){
            return response
        }else{
            Element.Message.error("错了哦，这是一条错误信息")

            return Promise.reject(response.data.msg)
        }
    },
    //前端请求失败时（400），返回的response里
    error => {
        console.log(error)
        if(error.response.data){
            error.message = error.response.data.msg
        }

       if(error.response.status === 401){
           store.commit("REMOVE_INFO")
           router.push("/login")
       }
       Element.Message.error(error.message)
       return Promise.reject(error)
    }

)