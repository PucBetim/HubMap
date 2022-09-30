import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { FormErrorComponent } from './form-error/form-error.component';
import { LoadingComponent } from './loading/loading.component';
import { ToneHexColorPipe } from './pipe/tone-hex-color.pipe';
import { LineSvgComponent } from './line-svg/line-svg.component';
import { DisplayCanvasComponent } from './display-map/display-canvas/display-canvas.component';
import { DisplayBlockComponent } from './display-map/display-block/display-block.component';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    FormErrorComponent,
    LoadingComponent,
    ToneHexColorPipe,
    LineSvgComponent,
    DisplayCanvasComponent,
    DisplayBlockComponent,
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    DragDropModule,
    MatIconModule
  ],
  exports: [
    FormErrorComponent,
    LoadingComponent,
    MatIconModule,
    ToneHexColorPipe,
    LineSvgComponent,
    DisplayCanvasComponent,
    DisplayBlockComponent,
  ]
})
export class SharedModule { }
