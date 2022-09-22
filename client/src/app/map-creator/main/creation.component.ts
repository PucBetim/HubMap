import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuTrigger } from '@angular/material/menu';

import { Map, Block } from '../../core/shared/posts/map';
import { Observable } from 'rxjs';
import { ComponentCanDeactivate } from 'src/app/core/services/guard.service';
import { PostService } from '../../core/shared/posts/post.service';
import { VisualCanvasComponent } from '../export-image/visual-canvas/visual-canvas.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { Post } from 'src/app/core/shared/posts/post';

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

  public map = new Map;
  public selectedBlock: Block;
  public blockSelected: boolean = false;
  public savedProgress: [Block[]] = [[]];
  public unsavedChanges: boolean = false;
  public carregando: boolean = false;

  constructor(
    private dialog: MatDialog,
    private postService: PostService
  ) { }

  ngOnInit(): void {
    var _map = JSON.parse(localStorage.getItem('mapa') || '{}');

    if (_map.blocks)
      this.map = _map;

    this.savedProgress = [JSON.parse(JSON.stringify(this.map.blocks))];

  }

  addNewBlock() {
    if (this.map.blocks?.length > 0)
      return;

    console.log(this.main.nativeElement.clientHeight)

    let _block = new Block;
    _block.backgroundColor = "#64b5f6"
    _block.fontColor = "#ffffff"
    _block.fontSize = "24px"
    _block.content = "Editar";
    _block.size.width = 250;
    _block.size.height = 100;
    _block.position.x = (this.main.nativeElement.clientWidth / 2) - (_block.size.width / 2);
    _block.position.y = (this.main.nativeElement.clientHeight / 2) - (_block.size.height / 2);


    if (this.map.blocks == null) {
      let _map = new Map;
      _map.blocks[0] = _block;
      this.map = _map
      return
    }
    this.map.blocks.push(_block);
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
      this.map.blocks = JSON.parse(JSON.stringify(this.savedProgress[this.savedProgress.length - 2]));
      this.savedProgress.splice(-1)
    }
  }

  saveProgress() {
    this.unsavedChanges = true;
    if (this.savedProgress.length > 15)
      this.savedProgress.splice(0, 1);
    const blocks = JSON.parse(JSON.stringify(this.map.blocks));
    this.savedProgress.push(blocks)
  }

  save() {
    this.unsavedChanges = false;
    localStorage.setItem('mapa', JSON.stringify(this.map));
  }

  post() {
    this.carregando = true;

    var post = new Post;

    let p = Object.assign({}, post)

    this.postService.post(p).subscribe({
      next: obj => {
        console.log('postado')
        this.carregando = false;
      }, error: error => {
        console.log('erro na postagem')
        this.carregando = false;
      }
    })
  }


  downloadImage() {
    var exportImageConfig = {
      disableClose: false,
      width: 'auto',
      height: 'auto',
      data: {
        blocks: this.map.blocks
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
