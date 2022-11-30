import { Author, Comment } from "./comment";

export class Post {
    title: string;
    description: string;
    private: boolean;
    id: string;
    likes: number;
    dislikes: number;
    views: number;
    map: Block[] = [];
    comments: Comment[];
    modified: Date;
    created: Date;
    author: Author;
}
export class Block {
    id?: string;
    blocks: Block[] = [];
    content: string = "";
    position: Position = new Position;
    size: Size = new Size;

    backgroundColor: string = "#ffffff";
    fontColor: string = "#000000";
    fontSize: number = 12;
    fontStyle: string = "normal";
    fontWeight: string = "normal";
    textDecoration: string = "none";
    textAlign:string = "center";
    borderRadius: string = "4";
}

export class Position {
    x: number = 0;
    y: number = 0;
}

export class Size {
    width: number = 200;
    height: number = 100;
}
