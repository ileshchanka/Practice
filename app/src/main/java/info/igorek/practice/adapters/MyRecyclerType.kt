package info.igorek.practice.adapters

sealed class MyRecyclerType {
    data class TextItem(val text: String) : MyRecyclerType()
    data class ImageItem(val resId: Int) : MyRecyclerType()
}