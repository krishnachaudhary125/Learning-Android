 fun main(){
        val name = "Krishna"

        val result = when(name){
            "Subodh" -> "Hello $name"
            "Krishna" -> "Hello $name"
            else -> "Unknown"
        }
        println(result)
    }