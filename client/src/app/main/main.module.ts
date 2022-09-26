import { SharedModule } from './../core/shared/shared.module';
import { LandingComponent } from './landing/landing.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainRoutes } from './main.routing';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DisplayCanvasComponent } from './display-map/display-canvas/display-canvas.component';
import { DisplayBlockComponent } from './display-map/display-block/display-block.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import {MatGridListModule} from '@angular/material/grid-list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';


@NgModule({
  declarations: [DisplayCanvasComponent, LandingComponent, DisplayBlockComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(MainRoutes),
    SharedModule,
    MatButtonModule,
    MatButtonToggleModule,
    FormsModule,
    DragDropModule, MatGridListModule,     MatProgressSpinnerModule,

  ]
})
export class MainModule { }
