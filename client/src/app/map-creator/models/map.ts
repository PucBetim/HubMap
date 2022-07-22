export class map {
    id: number;
    blocks: block[] = [];
}
export class block {
    id: number;
    content: string;
    position: position = new position;
    //img: ImageBitmap;
    color: number;
    fontSize: number;
    fontStyle: string;
    blocks: block[];
}

export class position {
    x: number = 0;
    y: number = 0;
}