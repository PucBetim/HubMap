import { Block, Position } from "../core/shared/posts/post";

export class GetLimitPoints {
    public points = new limitPoints;
    getClosestFartest(blocks: Block[]) {

        blocks.forEach(b => {
            //Closest
            if (!this.points.closestPoint.x) { this.points.closestPoint.x = b.position.x }
            else
                this.points.closestPoint.x = this.points.closestPoint.x < b.position.x ? this.points.closestPoint.x : b.position.x;

            if (!this.points.closestPoint.y) { this.points.closestPoint.y = b.position.y }
            else
                this.points.closestPoint.y = this.points.closestPoint.y < b.position.y ? this.points.closestPoint.y : b.position.y;
            //Closest end

            //Farest
            if (!this.points.farestPoint.x) { this.points.farestPoint.x = (b.position.x + b.size.width) }
            else
                this.points.farestPoint.x = this.points.farestPoint.x > (b.position.x + b.size.width) ? this.points.farestPoint.x : (b.position.x + b.size.width);

            if (!this.points.farestPoint.y) { this.points.farestPoint.y = (b.position.y + b.size.height) }
            else
                this.points.farestPoint.y = this.points.farestPoint.y > (b.position.y + b.size.height) ? this.points.farestPoint.y : (b.position.y + b.size.height);
            //Farest End

            this.getClosestFartest(b.blocks)
        });
        return this.points;
    }
}

class limitPoints {
    closestPoint = new Position;
    farestPoint = new Position;
}

// import { Block, Position } from "../core/shared/posts/post";

// export class GetLimitPoints {
//     public points = new limitPoints;

//     getClosestFartest(blocks:Block){
//         var block_: Block[] = [blocks];
//         return this.getPoints(block_);
//     }

//     getPoints(blocks: Block[]) {
//         blocks.forEach(b => {
//             //Closest
//             if (!this.points.closestPoint.x) { this.points.closestPoint.x = b.position.x }
//             else
//                 this.points.closestPoint.x = this.points.closestPoint.x < b.position.x ? this.points.closestPoint.x : b.position.x;

//             if (!this.points.closestPoint.y) { this.points.closestPoint.y = b.position.y }
//             else
//                 this.points.closestPoint.y = this.points.closestPoint.y < b.position.y ? this.points.closestPoint.y : b.position.y;
//             //Closest end

//             //Farest
//             if (!this.points.farestPoint.x) { this.points.farestPoint.x = (b.position.x + b.size.width) }
//             else
//                 this.points.farestPoint.x = this.points.farestPoint.x > (b.position.x + b.size.width) ? this.points.farestPoint.x : (b.position.x + b.size.width);

//             if (!this.points.farestPoint.y) { this.points.farestPoint.y = (b.position.y + b.size.height) }
//             else
//                 this.points.farestPoint.y = this.points.farestPoint.y > (b.position.y + b.size.height) ? this.points.farestPoint.y : (b.position.y + b.size.height);
//             //Farest End

//             this.getPoints(b.blocks)
//         });
//         return this.points;
//     }
// }

// class limitPoints {
//     closestPoint = new Position;
//     farestPoint = new Position;
// }