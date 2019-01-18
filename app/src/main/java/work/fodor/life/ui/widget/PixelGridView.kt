package work.fodor.life.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import work.fodor.life.domain.model.World


class PixelGridView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val pixelPaint by lazy {
        Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
        }
    }
    private val gridPaint by lazy {
        Paint().apply {
            color = Color.LTGRAY
        }
    }
    private var numberOfColumns: Int = 0
    private var numberOfRows: Int = 0
    private var cellSize: Int = 0
    private var showGrid = true
    private lateinit var cells: Array<BooleanArray>

    fun setGridData(world: World) {
        numberOfColumns = world.numberOfColumns
        numberOfRows = world.numberOfRows
        cells = world.cells.clone()

        calculateCellSize()
    }

    fun setShowGrid(value: Boolean) {
        this.showGrid = value
        invalidate()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        calculateCellSize()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        if (numberOfColumns == 0 || numberOfRows == 0) {
            return
        }

        // center align
        canvas.translate((width - cellSize * numberOfColumns) / 2f, (height - cellSize * numberOfRows) / 2f)

        //draw active pixels
        for (x in 0 until numberOfColumns) {
            for (y in 0 until numberOfRows) {
                if (cells[x][y]) {
                    canvas.drawRect(
                        cellSizeMultipliedBy(x), cellSizeMultipliedBy(y),
                        cellSizeMultipliedBy(x + 1), cellSizeMultipliedBy(y + 1),
                        pixelPaint
                    )
                }
            }
        }

        // draw grid lines
        if (showGrid) {
            for (i in 1 until numberOfColumns) {
                canvas.drawLine(
                    cellSizeMultipliedBy(i),
                    0f,
                    cellSizeMultipliedBy(i),
                    cellSizeMultipliedBy(numberOfRows),
                    gridPaint
                )
            }

            for (i in 1 until numberOfRows) {
                canvas.drawLine(
                    0f,
                    cellSizeMultipliedBy(i),
                    cellSizeMultipliedBy(numberOfColumns),
                    cellSizeMultipliedBy(i),
                    gridPaint
                )
            }
        }
    }


    // Internal

    private fun calculateCellSize() {
        if (numberOfColumns == 0 || numberOfRows == 0) {
            return
        }

        cellSize = Math.min(width / numberOfColumns, height / numberOfRows)

        invalidate()
    }

    private fun cellSizeMultipliedBy(multiplier: Int) = (cellSize * multiplier).toFloat()

}