export class map {
    id: number;
    blocks: block[] = [];
}
export class block {
    id: number;
    content: string;
    position: position = new position;
    size: size = new size;
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

export class size{
    width: number = 0;
    height: number = 0;
}