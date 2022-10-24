import { Position } from './../../core/shared/posts/post';
import { Component, ElementRef, HostListener, OnInit, ViewChild, EventEmitter, AfterContentInit, AfterViewInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuTrigger } from '@angular/material/menu';

import { Post, Block } from '../../core/shared/posts/post';
import { Observable, Subscription } from 'rxjs';
import { ComponentCanDeactivate } from 'src/app/core/services/guard.service';
import { PostService } from '../../core/shared/posts/post-blocks.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreatePostComponent } from '../create-post/create-post.component';
import { VisualCanvasComponent } from 'src/app/core/shared/export-image/visual-canvas/visual-canvas.component';
import { CanvasService } from 'src/app/core/services/canvas.service';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit, ComponentCanDeactivate {

  @ViewChild('main') main: ElementRef;
  @ViewChild('scrollLink') scrollLink: ElementRef;
  @ViewChild('canvas') canvas: ElementRef;

  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    return !this.unsavedChanges
  }

  public post = new Post;
  public canvasSize = CanvasService.canvasSize;

  public selectedBlock: Block;
  public savedProgress: [Block[]] = [[]];

  public unsavedChanges: boolean = false;
  public loading: boolean = false;
  public blockSelected: boolean = false;
  public editorMode: boolean = false;

  public sub: Subscription;
  public id: string;
  public rootBlockId: string;

  constructor(private route: ActivatedRoute,
    private dialog: MatDialog,
    private postService: PostService,
    private snackBar: MatSnackBar,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.getPost();
  }

  scrollToRoot() {
    if (!this.blockSelected)
      this.scrollLink.nativeElement.scrollIntoView({
        behavior: 'auto',
        block: 'center',
        inline: 'center'
      });
  }

  getPost() {
    this.loading = true;
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
            this.router.navigate(['/creator'])
          }
        })
    }
    else {
      _post = JSON.parse(localStorage.getItem('post')!);
      if (_post)
        this.post = _post;
      localStorage.removeItem('post');
      this.loading = false;
    }
  }


  getBlocks(post: Post) {
    this.loading = true;
    this.postService.getPostBlocks(post.id).subscribe({
      next: result => {
        if (result.body != null)
          post.map = [result.body]
        this.post = post;
        this.rootBlockId = this.post.map[0].id!;
        this.savedProgress = [JSON.parse(JSON.stringify(this.post.map))];
        this.loading = false;
      }, error: error => {
        this.snackBar.open(error.errors);
        this.loading = false;
      }
    })
  }

  addNewBlock() {
    if (this.post.map?.length > 0)
      return;

    let _block = new Block;
    _block.backgroundColor = "#64b5f6"
    _block.fontColor = "#ffffff"
    _block.fontSize = 18;
    _block.content = "Editar";
    _block.size.width = 125;
    _block.size.height = 50;
    _block.position.x = (this.canvasSize.width + _block.size.width) / 2;
    _block.position.y = (this.canvasSize.height + _block.size.height) / 2;

    this.post.map.push(_block);
    this.saveProgress();
  }

  finishedLoading() {
    this.scrollToRoot();
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
        texto: "Tem certeza que deseja deletar o bloco selecionado? Todos os blocos filhos também serão excluídos.",
        danger: true,
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
      this.post.map = JSON.parse(JSON.stringify(this.savedProgress[this.savedProgress.length - 2]));
      this.savedProgress.splice(-1)
    }
  }

  saveProgress() {
    this.unsavedChanges = true;
    if (this.savedProgress.length > 15)
      this.savedProgress.splice(0, 1);
    const blocks = JSON.parse(JSON.stringify(this.post.map));
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
        editorMode: this.editorMode,
        rootBlockId: this.rootBlockId
      }
    };

    const dialogRef = this.dialog.open(CreatePostComponent, createPostConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.link) { // Não logado
          this.save();
          this.unsavedChanges = false;
          this.router.navigate([result.link]);
        }
        else {  //Logado
          this.snackBar.open(result.msg, "Ok", {
            duration: 2000
          })
          localStorage.removeItem('post');
          if (result.id) {
            this.router.navigate([`creator/${result.id}`])
            this.getPost();
          }
          this.unsavedChanges = false;
        }
      }
    });
  }

  downloadImage() {
    var exportImageConfig = {
      disableClose: false,
      width: 'auto',
      height: 'auto',
      data: {
        blocks: this.post.map
      }
    };

    const dialogRef = this.dialog.open(VisualCanvasComponent, exportImageConfig);
    dialogRef.afterClosed().subscribe();
  }
}
