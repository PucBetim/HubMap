import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuTrigger } from '@angular/material/menu';

import { Post, Block } from '../../core/shared/posts/post';
import { Observable, Subscription } from 'rxjs';
import { ComponentCanDeactivate } from 'src/app/core/services/guard.service';
import { PostService } from '../../core/shared/posts/post-blocks.service';
import { VisualCanvasComponent } from '../export-image/visual-canvas/visual-canvas.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreatePostComponent } from '../create-post/create-post.component';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit, ComponentCanDeactivate {

  @ViewChild('editTrigger') editTrigger: MatMenuTrigger;
  @ViewChild('main') main: ElementRef;

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return !this.unsavedChanges
  }

  public post = new Post;

  public selectedBlock: Block;
  public savedProgress: [Block[]] = [[]];

  public unsavedChanges: boolean = false;
  public carregando: boolean = false;
  public blockSelected: boolean = false;
  public editorMode: boolean = false;

  public sub: Subscription;
  public id: number;

  constructor(private route: ActivatedRoute,
    private dialog: MatDialog,
    private postService: PostService,
    private router: Router,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.carregando = true;
    var _post = new Post;
    this.sub = this.route.params.subscribe(
      params => {
        this.id = params['id'];
      });

    if (this.id) {
      this.postService.getPostById(this.id).subscribe(
        {
          next: result => {
            _post = result.body;
            this.editorMode = true;
            this.getBlocks(_post);
          },
          error: error => {
            this.snackBar.open(error.errors);
            this.carregando = false;
          }
        })
    }
    else this.carregando = false
  }

  getBlocks(post: Post) {
    this.carregando = true;
    this.postService.getPostBlocks(post.id).subscribe({
      next: result => {
        post.blocks = result.body
        if (post.blocks)
          this.post = post;
        this.savedProgress = [JSON.parse(JSON.stringify(this.post.blocks))];
        this.carregando = false;
      }, error: error => {
        this.snackBar.open(error.errors);
        this.carregando = false;
      }
    })
  }

  addNewBlock() {
    if (this.post.blocks?.length > 0)
      return;

    console.log(this.main.nativeElement.clientHeight)

    let _block = new Block;
    _block.backgroundColor = "#64b5f6"
    _block.fontColor = "#ffffff"
    _block.fontSize = 24;
    _block.content = "Editar";
    _block.size.width = 250;
    _block.size.height = 100;
    _block.position.x = (this.main.nativeElement.clientWidth / 2) - (_block.size.width / 2);
    _block.position.y = (this.main.nativeElement.clientHeight / 2) - (_block.size.height / 2);


    if (this.post.blocks == null) {
      let _post = new Post;
      _post.blocks[0] = _block;
      this.post = _post
      return
    }
    this.post.blocks.push(_block);
    this.saveProgress();
  }


  selectBlock(block: Block) {
    this.selectedBlock = block;
    this.blockSelected = true;
  }

  unselectBlock() {
    this.blockSelected = false;
  }

  onDeleteBlock(blocks: Block[]) {
    var confirmDeleteConfig = {
      disableClose: false,
      width: 'auto',
      data: {
        titulo: "Deletar Bloco",
        texto: "Tem certeza que deseja deletar o bloco selecionado? Todos os blocos filhos também serão excluídos."
      }
    };

    if (this.blockSelected) {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, confirmDeleteConfig);
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.deleteBlockRecursive(blocks)
          this.saveProgress();
        }
      });
    }
    this.blockSelected = false;
  }

  deleteBlockRecursive(blocks: Block[]) {
    for (let i = 0; i < blocks.length; i++) {
      if (blocks[i] == this.selectedBlock) {
        const index = blocks.indexOf(this.selectedBlock, 0);
        if (index > -1) {
          blocks.splice(index, 1);
        }
      }
      else
        this.deleteBlockRecursive(blocks[i].blocks)
    }
  }

  undo() {
    this.unselectBlock();
    if (this.savedProgress.length > 1) {
      this.post.blocks = JSON.parse(JSON.stringify(this.savedProgress[this.savedProgress.length - 2]));
      this.savedProgress.splice(-1)
    }
  }

  saveProgress() {
    this.unsavedChanges = true;
    if (this.savedProgress.length > 15)
      this.savedProgress.splice(0, 1);
    const blocks = JSON.parse(JSON.stringify(this.post.blocks));
    this.savedProgress.push(blocks)
  }

  save() {
    this.unsavedChanges = false;
    localStorage.setItem('post', JSON.stringify(this.post));
  }

  createPost() {
    var createPostConfig = {
      disableClose: false,
      width: '300px',
      height: 'auto',
      data: {
        post: this.post,
        editorMode: this.editorMode
      }
    };

    const dialogRef = this.dialog.open(CreatePostComponent, createPostConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open(result, "Ok", {
          duration: 2000
        })
        this.unsavedChanges = false;
      }
    });
  }

  downloadImage() {
    var exportImageConfig = {
      disableClose: false,
      width: 'auto',
      height: 'auto',
      data: {
        blocks: this.post.blocks
      }
    };

    const dialogRef = this.dialog.open(VisualCanvasComponent, exportImageConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Imagem Salva')
      }
    });
  }
}
