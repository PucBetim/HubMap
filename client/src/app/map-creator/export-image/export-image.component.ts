import { block } from './../models/map';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import html2canvas from 'html2canvas';

@Component({
  selector: 'export-image',
  templateUrl: './export-image.component.html',
  styleUrls: ['./export-image.component.scss']
})
export class ExportImageComponent implements OnInit {

  @ViewChild('canvas') canvas: ElementRef;
  @ViewChild('downloadLink') downloadLink: ElementRef;

  public blocks: block[] = [];

  constructor(public dialogRef: MatDialogRef<ExportImageComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  ngOnInit(): void {
    this.blocks = this.data.blocks;
    this.downloadImage();
  }

  downloadImage() {
    html2canvas(this.canvas.nativeElement).then(canvas => {
      this.canvas.nativeElement.src = canvas.toDataURL();
      this.downloadLink.nativeElement.href = canvas.toDataURL('image/png');
      this.downloadLink.nativeElement.download = 'mapa-mental.png';
      this.downloadLink.nativeElement.click();
    });

  }
}
