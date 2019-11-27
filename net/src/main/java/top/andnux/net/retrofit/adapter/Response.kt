package top.andnux.net.retrofit.adapter

class Response<T>(
    var data: T?,
    var code: Int,
    var message: String
)