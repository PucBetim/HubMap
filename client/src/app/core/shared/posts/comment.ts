export class Comment {
    author: Author;
    content: string;
    dislikes: number;
    likes: number;
    repliedTo: Comment;
    timestamp: Date;
}

export class Author {
    email: string;
    name: string;
    nick: string;
}