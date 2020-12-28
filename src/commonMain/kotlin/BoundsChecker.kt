import com.soywiz.korge.tiled.TiledMap
import com.soywiz.korge.tiled.TiledMapView

class BoundsChecker(tiledMapView: TiledMapView) {
    private val collisionLayer = tiledMapView.tiledMap.data.tileLayers.first()

//    fun checkMovementPossibility(x: Double, y: Double, tileWidth: Int, tileHeight: Int): Boolean {
//        if (isBlockingObject(x, y, tileWidth, tileHeight)) return false
//        return true
//    }

    //TODO добавить еще якорные точки, а то ужас какой-то
    fun isBlockingObject(x: Double, y: Double, tileWidth: Int, tileHeight: Int): Boolean {
        val tilePosX = x.toInt() / tileWidth
        val tilePosY = y.toInt() / tileHeight

        return collisionLayer.map[tilePosX, tilePosY].value != 0
    }
}