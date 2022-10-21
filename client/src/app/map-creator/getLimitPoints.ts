import { Block, Position } from "../core/shared/posts/post";

export class GetLimitPoints {
    getClosestFartest(blocks: Block[], points?: limitPoints) {

        if (!points)
            points = new limitPoints;

        blocks.forEach(b => {
            
            //Closest
            if (points!.closestPoint.x == 0) { points!.closestPoint.x = b.position.x }
            else
                points!.closestPoint.x = points!.closestPoint.x < b.position.x ? points!.closestPoint.x : b.position.x;

            if (points!.closestPoint.y == 0) { points!.closestPoint.y = b.position.y }
            else
                points!.closestPoint.y = points!.closestPoint.y < b.position.y ? points!.closestPoint.y : b.position.y;
            //Closest end

            //Farest
            if (points!.farestPoint.x == 0) { points!.farestPoint.x = (b.position.x + b.size.width) }
            else
                points!.farestPoint.x = points!.farestPoint.x > (b.position.x + b.size.width) ? points!.farestPoint.x : (b.position.x + b.size.width);

            if (points!.farestPoint.y == 0) { points!.farestPoint.y = (b.position.y + b.size.height) }
            else
                points!.farestPoint.y = points!.farestPoint.y > (b.position.y + b.size.height) ? points!.farestPoint.y : (b.position.y + b.size.height);
            //Farest End

            this.getClosestFartest(b.blocks, points)
        });

        return points;
    }
}

class limitPoints {
    closestPoint = new Position;
    farestPoint = new Position;
}
