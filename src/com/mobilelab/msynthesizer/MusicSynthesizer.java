package com.mobilelab.msynthesizer;

import com.mobilelab.synth.SynthManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ViewFlipper;

public class MusicSynthesizer extends Activity implements OnTouchListener, OnCheckedChangeListener {
	
	public static String Tag = "MUSIC_SYNTHESIZER";
	
	private ViewFlipper flipper;
	private DrawView drawView;
	
	private SynthManager sm;
	
	private RadioGroup waveGroup;
	private RadioButton sqrBtn;
	private RadioButton triBtn;
	private RadioButton sawBtn;
	private RadioButton sinBtn;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        drawView = (DrawView) findViewById(R.id.XYPad);
       
        drawView.setOnTouchListener(this);
        
        waveGroup = (RadioGroup) this.findViewById(R.id.WaveForms);
        waveGroup.setOnCheckedChangeListener(this);
        sqrBtn = (RadioButton) this.findViewById(R.id.SquareWave);
        triBtn = (RadioButton) this.findViewById(R.id.TriangleWave);
        sawBtn = (RadioButton) this.findViewById(R.id.SawWave);
        sinBtn = (RadioButton) this.findViewById(R.id.SineWave);        
        
        /*create buttons for switching between option panels*/
        ImageButton button1 = (ImageButton) findViewById(R.id.panel1B);
        ImageButton button2 = (ImageButton) findViewById(R.id.panel2B);
        ImageButton button3 = (ImageButton) findViewById(R.id.panel3B);
        //ImageButton button4 = (ImageButton) findViewById(R.id.panel4B);

        /* if button1 is pressed, display 1st panel*/
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	if (flipper.getDisplayedChild() != 0){
                flipper.setInAnimation(inFromRightAnimation());
               // flipper.setOutAnimation(outToRightAnimation());
                flipper.setDisplayedChild(0);  
            	}
            }
        });

        /* if button2 is pressed, display 2st panel*/
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	/*only do animation if panel isn't currently displayed*/
            	if (flipper.getDisplayedChild() != 1){
                flipper.setInAnimation(inFromRightAnimation());
                //flipper.setOutAnimation(outToRightAnimation());
                flipper.setDisplayedChild(1);   
            	}
            }
        });
       
        /* if button3 is pressed, display 3srd panel*/
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	/*only do animation if panel isn't currently displayed*/
            	if (flipper.getDisplayedChild() != 2){
                flipper.setInAnimation(inFromRightAnimation());
                //flipper.setOutAnimation(outToLeftAnimation());
                flipper.setDisplayedChild(2); 
            	}
            }
        });
        
    /*     if button4 is pressed, display 4th panel
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	only do animation if panel isn't currently displayed
            	if (flipper.getDisplayedChild() != 3){
                flipper.setInAnimation(inFromRightAnimation());
                //flipper.setOutAnimation(outToLeftAnimation());
                flipper.setDisplayedChild(3); 
            	}
            }
        });*/
        
        sm = new SynthManager();
	}
    
    
    /*** inFromRightAnimation
     *   Animation for panels to "slide in" from the right when their button is pressed
     *   @return inFromRight - animation to be used for panels to slide in
     ***/
    private Animation inFromRightAnimation() {
    	Animation inFromRight = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
    	inFromRight.setDuration(200);
    	inFromRight.setInterpolator(new AccelerateInterpolator());
    	return inFromRight;
    }
  
   /*** outToLeftAnimation
     *   Animation for panels to "slide out" to the left when another panel button is pressed
     *   @return outtoLeft - animation to be used for panels to slide out
     ***/
     private Animation outToLeftAnimation() {
    	Animation outtoLeft = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
    	outtoLeft.setDuration(500);
    	outtoLeft.setInterpolator(new AccelerateInterpolator());
    	return outtoLeft;
    }

	//@Override
	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		dumpEvent(event);
		int id = 0;
		/* if(actionCode == MotionEvent.ACTION_POINTER_DOWN) {
			id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			Log.d(Tag, "Adding Pointer " + id);
			sm.addSoundSource(id);
		} else if (actionCode == MotionEvent.ACTION_POINTER_UP) {
			id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			sm.removeSoundSource(id);
		}*/
		
		switch(actionCode) {
		case MotionEvent.ACTION_DOWN:
			id = event.getPointerId(0);
			sm.addSoundSource(id);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			Log.d(Tag, "Adding Pointer " + id);
			sm.addSoundSource(id);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d(Tag, "Action Move");
			for(int i = 0; i < event.getPointerCount(); i++) {
				id = event.getPointerId(i);
				float freq = 400 + event.getY(i) * 4;
				sm.setSoundSourceFreq(id, freq);
			}
			break;
		case MotionEvent.ACTION_UP:
			id = event.getPointerId(0);
			sm.removeSoundSource(id);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			sm.removeSoundSource(id);
			break;
		}
		
		return true;
	}
	
	//@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(group.getId() == waveGroup.getId()) {
			if(checkedId == sqrBtn.getId()) {
				sm.setCurrentWaveShape(SynthManager.SQUARE_WAVE);
			} else if (checkedId == triBtn.getId()) {
				sm.setCurrentWaveShape(SynthManager.TRIANGLE_WAVE);
			} else if (checkedId == sawBtn.getId()) {
				sm.setCurrentWaveShape(SynthManager.SAW_WAVE);
			} else {
				sm.setCurrentWaveShape(SynthManager.SINE_WAVE);
			}
		}
	}
	
	/* private void dumpEvent(MotionEvent event) {
		 String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		 StringBuilder sb = new StringBuilder();
		 int action = event.getAction();
		 int actionCode = action;
		 sb.append("event ACTION_").append(names[actionCode]);
		 sb.append("[Coord: " + event.getX() + ", " + event.getY() + "]");
		 Log.d("MS", sb.toString());
	 }*/
	
	private void dumpEvent(MotionEvent event) {
		 String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
				 "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
		 StringBuilder sb = new StringBuilder();
		 int action = event.getAction();
		 int actionCode = action & MotionEvent.ACTION_MASK;
		 sb.append("event ACTION_" ).append(names[actionCode]);
		 if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				 || actionCode == MotionEvent.ACTION_POINTER_UP) {
			 sb.append("(pid " ).append(
					 action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			 sb.append(")" );
		 }
		 sb.append("[" );
		 for (int i = 0; i < event.getPointerCount(); i++) {
			 sb.append("#" ).append(i);
			 sb.append("(pid " ).append(event.getPointerId(i));
			 sb.append(")=" ).append((int) event.getX(i));
			 sb.append("," ).append((int) event.getY(i));
			 if (i + 1 < event.getPointerCount())
				 sb.append(";" );
		 }
		 sb.append("]" );
		 Log.d("MSX", sb.toString());
	 }
}  