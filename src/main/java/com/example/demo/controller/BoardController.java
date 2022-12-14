package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.DefaultNamingPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.BoardCtgDto;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardDtoList;
import com.example.demo.dto.BoardFileDto;
import com.example.demo.dto.BoardGetDto;
import com.example.demo.dto.BoardListReqDto;
import com.example.demo.dto.BoardSetDto;
import com.example.demo.dto.Paging;
import com.example.demo.dto.ReplyDto;
import com.example.demo.service.BoardService;

@Controller
public class BoardController {

	String file_ref_tbl_board = "bt_tb_board";
	String file_ref_key_board = "board_attach_file";

	@Autowired
	private BoardService bs;

	@Autowired
	ServletContext context;
	/**
	 * <PRE>
	 * 1. ?????? : TB_MEMBER ?????? ?????? _ JSP??? / MemberDto??? ??????V
	 * 2. ????????????  : TB_MEMBER ?????? ?????? _ JSP??? / MemberDto??? ??????
	 * 3. Comment   :
	 * </PRE>
	 *
	 * @Method Name : memberList
	 * @date : 2022. 6. 21.
	 * @author : AN
	 * @history :
	 *     -----------------------------------------------------------------------
	 *     ????????? ????????? ???????????? ----------- -------------------
	 *     --------------------------------------- 2022. 6. 21. AN ????????????
	 *     -----------------------------------------------------------------------
	 *
	 */
	@RequestMapping(value = "/"	 , method = {RequestMethod.GET, RequestMethod.POST} )
	public ModelAndView main(Model model, HttpServletRequest request, BoardListReqDto brdto) {
		System.out.println("~~~~~~~~ ????????? ??????~~~~~~~~~");

		ModelAndView mav = new ModelAndView();

		// TODO ???????????? ?????? ?????? ????????????
		//BoardCtgDto bcdto = bs.categoryGet();
		List<BoardCtgDto> categoryList =bs.categoryGet();
		mav.addObject("category", categoryList);

		//?????? ?????? ??????
		mav.addObject("brdto", brdto);
		System.out.println("???????????? ??????!!!!!!!!!!!!!!!!!!!" + brdto);

		mav.setViewName("main");

		return mav;

	}

	@PostMapping(value = "/boardList" /* , method =RequestMethod.GET */)
	@ResponseBody
	public BoardDtoList boardList(BoardListReqDto bkdto) {

			// ????????? ????????????
			BoardDtoList boardDtoList = new BoardDtoList();
			List<BoardDto> boardList = bs.boardList(bkdto);

			int count = bs.getBoardCount(bkdto);

			// ????????? ????????? ????????????
			// BoardListReqDto boardkeyList = new BoardListReqDto();


			// list ?????? ?????? ?????? ?????? ???
			for (BoardDto board : boardList) {
				// System.out.println("\n!!!! " + board);
				// System.out.println("\n!!!!!!! : " + board.getRef_pk());
			}
			// System.out.println(boardList);

			// System.out.println(bkdto.getKeyword() + bkdto.getType() +
			// bkdto.getCategory());
			boardDtoList.setBoardList(boardList);
			boardDtoList.setCount(count); // boardDtoList ??? ????????? ajax??? ?????????



			return boardDtoList;
	}

	/* ??????????????? ????????? !
	 * @PostMapping(value="/boardDetail") public String
	 * boardDetail( @RequestParam(value="board_no") int board_no, Model model)
	 * {//main.jsp function(boardDetail)????????? ????????? board_no??? ????????? ?????????
	 * System.out.println("######## : " );
	 *
	 * System.out.println("######## : " + board_no); BoardGetDto getboardDetail = new
	 * BoardGetDto();
	 *
	 * getboardDetail=bs.getBoardOne(board_no);
	 *
	 * model.addAttribute("getBoard", getboardDetail); //board_no ??? ?????? ajax??? ????????? ??????
	 * boardDetail??? ?????????????????? ????????? ?????? ????????? return "boardDetail"; }
	 */


	//????????? ???????????? ajax ???????????? ?????????
	@PostMapping(value="/boardDetail")
	public String boardDetail( @RequestParam(value="board_no") int board_no,  Model model, BoardListReqDto brdto) {//main.jsp function(boardDetail)????????? ????????? board_no??? ????????? ?????????


			System.out.println("######## : " + board_no);
		//	BoardGetDto getboardDetail = new BoardGetDto();  ????????????
		//	getboardDetail.setBoard_no(board_no);
			System.out.println("???????????? ??????!!!!!!!~~~~~~~~~~~~!!!!!!!!!!!!" + brdto);

			model.addAttribute("brdto", brdto);
			model.addAttribute("board_no", board_no); //board_no ??? ?????? ajax??? ????????? ?????? boardDetail??? ?????????????????? ????????? ?????? ????????? //name ????????? ?????? ??????
			return "boardDetail";
	}

    //Ajax ??? Ajax????????? ?????? ??????
	@GetMapping(value="/getboardDetail")
	@ResponseBody
	public BoardGetDto getboardDetail(@RequestParam(value="board_no", required=true) Integer board_no
			, Model model){
			System.out.println("dfdfdsfd:!!!!!!!!!!!!!" + board_no );

			int view_cnt=bs.updateViewcnt(board_no);  //??????????????? ?????? ????????? ?????? ?????? cunt ??? ??????
			BoardGetDto bgdto =bs.getBoardOne(board_no);


			//?????? ?????? ????????? ??????
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//String date = df.format(bgdto.getReg_dt());
			//	int view_cnt=bs.updateViewcnt(bgdto.getBoard_no());

			int filecount = 0;
			String ref_pk = Integer.toString(board_no);
			List<BoardFileDto> fileList = bs.getFileOne(ref_pk);
			//fileList size ????????? ????????? ????????? ?????? ????????? ????????? ?????? ??? ???????????? ????????? ????????? ????????? ??? ???????????? 1????????? ????????? ??? ??????
			if(fileList.size() > 0) {
				bgdto.setFileList(fileList);  //????????? List??? ???????????? ????????? ????????? board_no ??? ????????? 3????????? ??????????????? List??? ???????????? boardGetDto ??? ????????????
			//	int filecount = fileList.size() ;*********
			//	bgdto.setFilecount(filecount);
			}

			System.out.println("!!!!!!!!!!!??? ????????? :  "+ bgdto.getView_cnt());
			System.out.println("board_no:" + board_no);
			System.out.println("????????? ??? :" + bgdto);
			//System.out.println("??????????????????"  + dateToStr);
			//System.out.println(date);
			//model.addAttribute("getBoard", bgdto);  ????????????

			//bgdto.setDate(date);




			return bgdto;
	}



	//password??? ?????????????????? ??????????????? ????????? ?????? ???????????? ????????????
	@GetMapping(value="/boardPassword")
	public String boardPassword(/*Model model , @RequestParam(value="board_no", required=true) Integer board_no */) {

			//System.out.println("???????????? ?????? ?????? " + board_no);
			//BoardGetDto bgdto = bs.selectPassword(board_no);

			//javascript?????? ?????? ????????? ????????? !
			//model.addAttribute("pass", bgdto.getPassword()); //?????? ?????? ????????? ??? ????????? ??????

			//model.addAttribute("board_no", board_no);
			return "boardPassword";
	}

	@PostMapping("/checkPass")
	@ResponseBody
	public BoardGetDto checkPass( Model model , @RequestParam("board_no") int board_no,
			@RequestParam(value= "board_pass", required=true) String password) {
		//System.out.println("boardCheckpass ?????? ?????? ??????  : " + board_no);
		//System.out.println("password ??????" +  password);

			String msg;
			//String getflag;
			BoardGetDto bgdto = bs.selectPassword(board_no);
			model.addAttribute("board_no", board_no);


			if(password.equals(bgdto.getPassword())) {
				msg="???????????? ?????? ";
				bgdto.setGflag("true");

			}else {
				model.addAttribute("message", "??????????????? ?????? ???????????? ????????? ?????????");
				bgdto.setGflag("false");
				msg="??????????????? ?????? ?????? ???????????? ?????? ??????????????? ";

			}

			bgdto.setMsg(msg);
			return bgdto;
	}



	//????????? ????????? ???????????? ?????? ??????????????? ?????????
	/*@PostMapping("/boardCheckpass")
	public String boardCheckpass( Model model , @RequestParam("board_no") int board_no,
			@RequestParam(value= "password", required=true) String password) {
		//System.out.println("boardCheckpass ?????? ?????? ??????  : " + board_no);
		//System.out.println("password ??????" +  password);

			BoardGetDto bgdto = bs.selectPassword(board_no);
			model.addAttribute("board_no", board_no);


			if(password.equals(bgdto.getPassword())) {
				return "boardCheckPass";
			}else {
				model.addAttribute("message", "??????????????? ?????? ???????????? ????????? ?????????");
				return "boardPassword";
			}

	}
	*/

	//ajax ???????????? ????????? - ????????? ?????? ??? ?????????
	@PostMapping("/boardUpdateForm")
	public String boardUpdateForm(Model model, @RequestParam("board_no") int board_no,BoardListReqDto brdto) {

		//board_no ??? ?????? ????????? ????????????
		BoardGetDto bgdto = new BoardGetDto();
		bgdto =bs.getBoardOne(board_no);
		//model.addAttribute("bgdto", bgdto);
		//model.addAttribute("password", bgdto.getPassword());  //password??? ???????????? ???????????????

		//??????????????????
		model.addAttribute("brdto", brdto);
		System.out.println("??????????????????????????????????????????????????? !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!???"  + brdto);
		//???????????? ?????????
		List<BoardCtgDto> categoryList =bs.categoryGet();


		int count=0;
		String ref_pk = Integer.toString(board_no);
		  List<BoardFileDto> fileList = bs.getFileOne(ref_pk);

		//fileList size ????????? ????????? ????????? ?????? ????????? ????????? ?????? ??? ???????????? ????????? ????????? ????????? ??? ???????????? 1????????? ????????? ??? ??????
		  if(fileList.size() >0) {
			  bgdto.setFileList(fileList);  //????????? List??? ???????????? ????????? ????????? board_no ??? ????????? 3????????? ??????????????? List??? ???????????? boardGetDto ??? ????????????

			//  model.addAttribute("save_file_nm1", fileList.get(0).getSave_file_nm());
			//  model.addAttribute("save_file_nm2", fileList.get(1).getSave_file_nm());
			 // model.addAttribute("save_file_nm3", fileList.get(2).getSave_file_nm());

			  for(BoardFileDto bfdto : fileList) { //fileList??? dto ??? ????????? ?????? ?????? ????????? x?????? ????????????
				  System.out.println(count + " ?????? " + bfdto);
				  count ++;
				 // model.addAttribute("ref_pk", bfdto.getRef_pk()); //????????????...?
				  System.out.println("\n\n\n SaveFileName : " + bfdto.getSave_file_nm());
				  //???????????? ?????? for??? ????????? ????????? ?????? ????????? ???????????? ?????? ?????????
				  System.out.println( model.addAttribute("count", count));
				  System.out.println("??????????????????????????????/n" +fileList.get(0).getFile_no() );

			  }

		  }
		  model.addAttribute("bfdto", fileList);
		  model.addAttribute("count", count); //?????? ????????? x?????? (delete)??????



		model.addAttribute("category", categoryList);
		model.addAttribute("board_no", board_no);

		//model.addAttribute("file_no", fileList.get(0).getFile_no());
		return "boardwrite";
	}

	//ajax ????????? ?????? ????????????
	@GetMapping("/updateBoard")
	@ResponseBody
	public BoardGetDto updateBoard(Model model, @RequestParam(value="board_no",required=true) Integer board_no) {
		//BoardGetDto bgdto = bs.boardUpdate(board_no);

		/* */
		  BoardGetDto bgdto = new BoardGetDto();
		  bgdto =bs.getBoardOne(board_no);

		  String ref_pk = Integer.toString(board_no);
		  List<BoardFileDto> fileList = bs.getFileOne(ref_pk);

		  if(fileList.size() > 0) {
		  bgdto.setFileList(fileList);  //????????? List??? ???????????? ????????? ????????? board_no ??? ????????? 3????????? ??????????????? List??? ???????????? boardGetDto ??? ????????????

		  }


			model.addAttribute("getBoard", bgdto);
			//model.addAttribute("board_no", board_no);

			return  bgdto;
	}

	//ajax ????????? ??????
	@PostMapping("/update")
	@ResponseBody
	public BoardSetDto update(Model model, BoardSetDto bsdto, HttpServletRequest request
			, MultipartFile[] uploadFile // <input> file type name?????? ??????????????? ?????????.
		) throws Exception { //???????????? ????????? service ??? ??????


		BoardSetDto resultDto = null;
		resultDto = bs.update(bsdto, uploadFile);
		//?????????????????? ????????? ????????? !!!


		/*
		// 0. MultipartFile[] ??? ????????? ??????????????? ?????? !
		System.out.println("UPLOADFILE ?????? : "+ uploadFile[0] );
		System.out.println("UPLOADFILE ?????? : "+ uploadFile[1] );

		// 1. uploadFile ??? null ?????? ?????? ??????????????? 1??? ????????? ????????? ?????????.

		String dirName = "C:/JAVA02/WEB04_coreBoard/src/main/webapp/upload/"; //????????? ?????? ??????
		BoardFileDto bfdto = new BoardFileDto();
		try {

		File folder = new File(dirName);  //dirName ?????? ????????????

		if (!folder.exists()) folder.mkdirs();  //????????? ????????? ???????????? ?????? ?????? dirName ??????????????????

		//??????????????? - ????????? ?????? ?????? ??????
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");  //????????? ????????? ?????? ??????
		int rand =(int)(Math.random()*1000);

		int fresult = 0;

		for( int i=0; i<uploadFile.length; i++ ) { //?????? ????????? ??? ??? 3?????? ??????????????? length ??? ?????? ????????? ?????????

			String fileName = uploadFile[i].getOriginalFilename(); //????????? ?????? ??????

			if( "".equals(fileName) || fileName == null ) continue;  //????????? ???????????? ???????????? ?????? ???

			//?????? ?????? ????????? ?????? ????????? ??????
			String ext 		= fileName.substring(fileName.lastIndexOf(".") +1);

			//?????? ?????? ??????
			String reName =sdf.format(System.currentTimeMillis()) + "_" +rand + "." + ext;

			//????????? ????????? ?????? ????????? ??????
			File destination = new File(dirName + File.separator + reName); //????????? ????????????

			//uploadFile[i].transferTo(destination);  //transferTo ?????? ???????????? ????????? file ??? ????????? ??????
			System.out.println("ORIGIN ?????? : " + destination);

			System.out.println("??????????????? : " + fileName);
			System.out.println("????????? : " +  ext);
			System.out.println("????????? ?????? ?????? : " + reName);

			//bfdto.setOrigin_file_nm( uploadFile[i].getOriginalFilename() ); // ???????????????

			//???????????? ?????????
			// 0. dto ????????? > ???????????? object ?????????
			// 1. set ?????? ?????? ?????????


			uploadFile[i].transferTo(destination);  //transferTo ?????? ???????????? ????????? file ??? ????????? ??????

			bfdto.setOrigin_file_nm(fileName);
			bfdto.setSave_file_nm(reName);
			bfdto.setSave_path(dirName);
			//bfdto.setSize(uploadFile[i].getSize());
			byte eee = (byte) uploadFile[i].getSize();
			bfdto.setSize(eee & 0xff);
			bfdto.setRef_tbl(file_ref_tbl_board);
			bfdto.setRef_pk("3");
			bfdto.setRef_key(file_ref_key_board);
			//System.out.println("?????????????????? " + bfdto.getSize() );
			bfdto.setExt(ext);
			bfdto.setOrd(i +1); //0?????? ???????????? ????????? +1??? ?????? ?????? 1,2,3

			fresult +=	bs.fileUploadInsert(bfdto);

		}

		} catch (Exception e){

		}
		// 2. localhost:8000/file/20220801/board/ ???????????? ??? ?????? ?????? ??????

		// 3.  for ??? ????????? ?????? ?????? ?????? -> db ?????? ( ????????? )

		// 3.  ?????????????????? ????????? ??????
*/
/////////////////////////////////////////////////////////////////????????? ????????? ?????? ???

		return resultDto;  //resultDto ??? ajax??? ??????
	}


	@PostMapping("/boardWriteForm")//{board_no} ??? ???????????? ?????? ????????? Get ?????? ?????????
	public String boardWriteForm(BoardListReqDto brdto, Model model) {
		//???????????? ??????
		model.addAttribute("brdto", brdto);

		//???????????? ?????????
		List<BoardCtgDto> categoryList =bs.categoryGet();
		model.addAttribute("category", categoryList);
		//System.out.println("category" + categoryList);
		System.out.println("??????????????????????????????????????????????????? !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!???"  + brdto);

		return "boardwrite";
	}

	//??????
	@PostMapping("/boardDeleteForm")
	@ResponseBody
		public int boardDeleteForm(BoardGetDto bgdto/*, BoardFileDto bfdto */) throws Exception{
		int result = 0;
		result = bs.boardAllDeleteForm(bgdto/* , bfdto */);






		return result;
	}

	//@DeleteMapping("boardFileDelete")
	@PostMapping("boardFileDelete")
	@ResponseBody
	public int boardFileDelete( @RequestParam(value = "file_no", required=true) int file_no , BoardFileDto bfdto
		/*, @RequestParam(value="board_no") int board_no*/) throws Exception {

			//int??? ????????? ????????? ???????????? ?????? ??? result ??? ????????? ??????

			int result = 0;
			result =bs.deleteBoardFile(file_no, bfdto);

			/*
			int result =0;
			bfdto = bs.selectFile(file_no);  //bfdto??? ???????????????????????? get?????? ?????????????????? ????????? save_nm, save_path??? ???????????? ????????? select ???
			System.out.println(bfdto.getSave_path() + bfdto.getSave_file_nm()  );
			String save_fileName = bfdto.getSave_path() + bfdto.getSave_file_nm();
			Path filePath = Paths.get(save_fileName);



				//?????? ??????
				try {

						Files.delete(filePath);
						System.out.println("???????????? ");

				} catch (NoSuchFileException e) {
					System.out.println("??????????????? ??????/??????????????? ????????????");
				} catch (IOException e) {

					e.printStackTrace();
				}

			System.out.println("??????????????????" + file_no);
			result = bs.boardFileDelete(file_no);
		*/
		return result;
	}


	@GetMapping("/filedownload")
	public void fileDownload(@RequestParam("file_no") int file_no, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//String directory = "C:/JAVA02/WEB04_coreBoard/src/main/webapp/upload/";
		//String files[] =new File(directory).list();

		//???????????? ??????
		BoardFileDto bfdto = bs.fileNumSelect(file_no);

		//????????? ???????????? (????????? ????????? , ?????? ?????? save????????????)
		String origin_fileName = bfdto.getOrigin_file_nm();
		String save_fileName = bfdto.getSave_file_nm();
		System.out.println("fileName" + save_fileName);

		//????????? ?????? ????????? ???????????? (????????? ???????????? )????????? ????????? ????????????
		String filePath = bfdto.getSave_path();
				//"C:/JAVA02/WEB04_coreBoard/src/main/webapp/upload/";

		//????????? ??????????????? ?????? ?????? ??????, ???????????? save_fileName ?????? ?????? ????????????????????? save????????? ??????
		File file = new File(filePath,save_fileName);

		//?????? ???????????? ????????? (?????????)
		//String mimeType = getServletContext().getMimeType(file.toString());
		String mimeType = bfdto.getExt();
		if(mimeType ==null ) { //????????? ???????????? ????????? ????????? ???????????? ????????? ??????
			response.setContentType("appliction/octet-stream");  //???????????? ????????? ??????!!!????????? ?????? ????????? ?????? ?????? ????????????????????? . ??????????????? ?????? octet-stram
		}//?????? ?????? ????????????????????? ??? ??? ??? ??????

		//?????? ???????????? ?????? ?????? ??????
		String downloadName =null;
		//????????? ??????????????? =MSIE ??????????????? ????????? ?????? ??????????????????
		if(request.getHeader("user-agent").indexOf("MSIE") == -1) {  //?????????????????? ???????????? ????????????
			downloadName = new String(origin_fileName.getBytes("UTF-8"), "8859_1"); //????????? 8859_1
		}else {
			downloadName =new String(origin_fileName.getBytes("EUC-KR"), "8859_1");
		}
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ downloadName + "\";");   //oringinName?????? ???????????? ?????? setHeader

		FileInputStream fileInputStream = new FileInputStream(file);
		ServletOutputStream servletOutputStream = response.getOutputStream();

		//????????????
		byte b[] = new byte[1024];
		int data =0;
		while((data = (fileInputStream.read(b, 0,b.length))) !=-1) {//b???????????? -1??? ????????? ??????
			servletOutputStream.write(b, 0, data); //b??? ???????????? ????????????

		}


//		int downloadresult = bs.downloadCount(file_no);
		int downloadresult = bs.BoardDownloadCount(file_no);


		servletOutputStream.flush();
		servletOutputStream.close();
		fileInputStream.close();

	}

///////////////////////////////////////////////////////////////////

	@GetMapping(value="/replylist")
	@ResponseBody
	public ReplyDto replyList(@RequestParam(value="board_no") int board_no) {
		//?????? ??????
		List<ReplyDto> replyList = bs.replyList(board_no);
		ReplyDto rdto = new ReplyDto();
		rdto.setReplyList(replyList);

		int reply_count = bs.replyCount(board_no);
		rdto.setReply_count(reply_count);

		return rdto;
	}//
	//?????? insert
	@PostMapping(value="/insertReply")
	@ResponseBody
	public int getboardDetail(@RequestParam(value="replycontent",required=true) String content, @RequestParam(value="reply_pw") String reply_password,
			@RequestParam(value="reply_nm") String reply_nm, @RequestParam(value="board_no" , required=true) Integer board_no) {

		System.out.println(content);
		System.out.println(reply_password);
		System.out.println(reply_nm);
		System.out.println("SDfsdf" + board_no);
		int result =0;
		ReplyDto rdto = new ReplyDto();
		rdto.setContent(content);
		rdto.setReply_password(reply_password);
		rdto.setReply_nm(reply_nm);
		rdto.setBoard_no(board_no);

		result =bs.insertReply(rdto);


		return result;
	}

	//?????? ???????????? ?????? ?????????
	@GetMapping(value="/replyPw")
	public String replyPw() {

			return "replyPw";
	}

	//?????? ???????????? ??????
	@PostMapping(value="/replypwcheck")
	@ResponseBody
	public ReplyDto replyPwCheck( @RequestParam(value= "reply_no" ,required=true) Integer reply_no,  @RequestParam(value ="replyPw") String replyPw) {

		ReplyDto rdto = bs.getSelectReply(reply_no);

		System.out.println("?????????????????? ??????" + replyPw);
		System.out.println("????????? ??????" + reply_no);
		System.out.println(rdto.getReply_password());
		String msg ="";
		String flag ="";
		if(rdto.getReply_password().equals(replyPw)) {
			msg ="?????????????????????????";
			flag ="true";
		}else if(!rdto.getReply_password().equals(replyPw)) {
			msg =" ??????????????? ???????????????";
			flag ="false";
		}else {
			msg ="??????????????? ???????????????";
			flag ="false";
		}

		rdto.setMsg(msg);
		rdto.setFlag(flag);

		return rdto;
	}

	@GetMapping(value="/replyUpdate")
	@ResponseBody
	public int replyUpdate(@RequestParam(value= "reply_no") int reply_no, ReplyDto rdto ) {
		System.out.println(rdto);
		System.out.println(reply_no);
		int result =0;

		result=bs.updateReply(rdto);

		System.out.println(result);


		return result;
	}
	@PostMapping(value="/replyDelete")
	@ResponseBody
	public int replyDelete(@RequestParam(value= "reply_no") int reply_no) {

		int result =0;

		result=bs.deleteReply(reply_no);

		return result;
	}

}
