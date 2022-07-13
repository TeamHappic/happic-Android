package happy.kiki.happic.module.dailyhappic.data

data class TagModel(
    val id: String,
    val date: String,
    val dayOfWeek: String,
    val time: String,
    val where: String,
    val who: String,
    val what: String
)
