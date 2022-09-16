import { VisualCanvasComponent } from '../export-image/canvas/visual-canvas.component';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuTrigger } from '@angular/material/menu';

import { map, block } from '../models/map';

@Component({
  selector: 'app-creation',
  templateUrl: './creation.component.html',
  styleUrls: ['./creation.component.scss']
})
export class CreationComponent implements OnInit {

  @ViewChild('editTrigger') editTrigger: MatMenuTrigger;
  @ViewChild('main') main: ElementRef;

  public map = new map;
  public selectedBlock: block;
  public blockSelected: boolean = false;
  public savedProgress: [block[]] = [[]];

  constructor(public dialog: MatDialog) { }

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

    let _block = new block;
    _block.backgroundColor = "#64b5f6"
    _block.fontColor = "#ffffff"
    _block.fontSize = "24px"
    _block.content = "Editar";
    _block.size.width = 250;
    _block.size.height = 100;
    _block.position.x = (this.main.nativeElement.clientWidth / 2) - (_block.size.width / 2);
    _block.position.y = (this.main.nativeElement.clientHeight / 2) - (_block.size.height / 2);


    if (this.map.blocks == null) {
      let _map = new map;
      _map.blocks[0] = _block;
      this.map = _map
      return
    }
    this.map.blocks.push(_block);
    this.saveProgress();
  }


  selectBlock(block: block) {
    this.selectedBlock = block;
    this.blockSelected = true;
  }

  unselectBlock() {
    this.blockSelected = false;
  }

  onDeleteBlock(blocks: block[]) {
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

  deleteBlockRecursive(blocks: block[]) {
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
    if (this.savedProgress.length > 15)
      this.savedProgress.splice(0, 1);
    console.log('salvo')
    const blocks = JSON.parse(JSON.stringify(this.map.blocks));
    this.savedProgress.push(blocks)
  }

  save() {
    localStorage.setItem('mapa', JSON.stringify(this.map));
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
