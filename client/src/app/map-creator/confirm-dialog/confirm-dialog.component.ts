import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
})
export class ConfirmDialogComponent implements OnInit {
  public texto: string;
  public titulo: string;
  public danger: boolean = false;

  public btnClass: string[];
  public titleClass: string[] = ['title-deco'];

  constructor(public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.texto = this.data.texto;
    this.titulo = this.data.titulo;
    this.danger = this.data.danger;

    if (this.danger) { 
      this.btnClass = ['btn-danger']; 
      this.titleClass= ['title-deco-danger']
    }
  }

}
