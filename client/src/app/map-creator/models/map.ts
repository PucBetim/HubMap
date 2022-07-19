export class map {
    id: number;
    blocks: block[];
}
export class block {
    id: number;
    content: string;
    coordX: number;
    cordY: number;
    //img: ImageBitmap;
    color: number;
    fontSize: number;
    fontStyle: string;
    blocks: block[];
}