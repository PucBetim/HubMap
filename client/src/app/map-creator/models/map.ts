export class map {
    id: number;
    blocks: block[] = [];
}
export class block {
    id: number;
    blocks: block[] = [];
    content: string = "Default";
    position: position = new position;
    size: size = new size;

    backgroundColor: string = "#ffffff";
    fontColor: string = "#000000";
    fontSize: string = "12px";
    fontStyle: string = "normal";
    fontWeight: string = "normal";
    textDecoration: string = "none";
    textAlign:string = "center";
    borderRadius: string = "4px";
}

export class position {
    x: number = 0;
    y: number = 0;
}

export class size {
    width: number = 200;
    height: number = 100;
}