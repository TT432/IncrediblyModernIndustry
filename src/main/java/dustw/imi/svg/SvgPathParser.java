package dustw.imi.svg;

import icyllis.modernui.math.Point;
import icyllis.modernui.math.PointF;

import java.util.function.Consumer;

/**
 * TODO 计划：XML 解析器      对接 MC 的资源管理从流内读取 XML 并解析
 *           shader 自动拼接 根据解析出来的 SVG 对象生成 shader
 * @author DustW
 **/
public class SvgPathParser {
    PointF point = new PointF();
    PointF movedPoint = new PointF();

    // 直线

    void m(float x, float y, boolean absolute) {
        if (absolute) {
            point.set(x, y);
        } else {
            point.offset(x, y);
        }
    }

    void l(float x, float y, boolean absolute) {
        if (absolute) {
            // TODO line      使用 movedPoint 并在最后修改 movedPoint
        } else {
            // TODO line
        }
    }

    void h(float x, boolean absolute) {
        l(x, 0, absolute);
    }

    void v(float y, boolean absolute) {
        l(0, y, absolute);
    }

    void z() {
        l(point.x, point.y, true);
    }

    // 曲线

    PointF c(float t, float c1x, float c1y, float c2x, float c2y, float x, float y, boolean absolute) {
        if (!absolute) {
            c1x += point.x;
            c1y += point.y;
            c2x += point.x;
            c2y += point.y;
            x += point.x;
            y += point.y;
        }

        return new PointF(movedPoint.x * (t - 1) * (t - 1) * (t - 1) + 3 * x * t * (1 - t) * (1 - t) + 3 * c1x * t * t * (1 - t) + c2x * t * t * t,
                movedPoint.y * (t - 1) * (t - 1) * (t - 1) + 3 * y * t * (1 - t) * (1 - t) + 3 * c1y * t * t * (1 - t) + c2y * t * t * t);
    }


}
