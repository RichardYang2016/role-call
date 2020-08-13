import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CastEditorV2 } from './cast-editor-v2.component';
import { RouterTestingModule } from '@angular/router/testing';

describe('CastEditorV2Component', () => {
  let component: CastEditorV2;
  let fixture: ComponentFixture<CastEditorV2>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        CastEditorV2,
        RouterTestingModule,
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CastEditorV2);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
