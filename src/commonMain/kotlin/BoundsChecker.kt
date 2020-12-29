import com.soywiz.korge.tiled.TiledMapView

class BoundsChecker(tiledMapView: TiledMapView) {
    private val collisionLayer = tiledMapView.tiledMap.data.tileLayers.first()

    fun isPlayerBlocked(
        x: Double,
        y: Double,
        dx: Double,
        dy: Double,
        tileWidth: Int,
        tileHeight: Int
    ): Boolean {
        return isBlockingObject(x + dx, y + dy, tileWidth, tileHeight)
    }

    private fun isBlockingObject(x: Double, y: Double, tileWidth: Int, tileHeight: Int): Boolean {
        val tilePosX = (x / tileWidth).toInt()
        val tilePosY = (y / tileHeight).toInt()

        return collisionLayer.map[tilePosX, tilePosY].value != 0
    }
}