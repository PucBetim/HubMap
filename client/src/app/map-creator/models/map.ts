export class map {
    id: number;
    blocks: block[] = [];
}
export class block {
    id: number;
    blocks: block[];
    content: string;
    position: position = new position;
    size: size = new size;
    backgroundColor: string = "white";
    color: number;
    fontSize: number;
    fontStyle: string = "normal";
    fontWeight: string = "normal";
    textDecoration: string = "none";


    //img: ImageBitmap;
    //selectedColor: string = "white";
}

export class position {
    x: number = 0;
    y: number = 0;
}

export class size {
    width: number = 0;
    height: number = 0;
}