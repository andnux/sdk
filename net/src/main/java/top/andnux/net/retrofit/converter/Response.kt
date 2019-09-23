package top.andnux.net.retrofit.converter

class Response<T>(
    var data: T?,
    var code: Int,
    var message: String
)