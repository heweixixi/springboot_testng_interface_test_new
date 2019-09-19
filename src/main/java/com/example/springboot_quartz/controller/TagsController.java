//package  com.weiming.scav2.app.tags;
//
//import  com.github.pagehelper.PageSerializable;
//import  com.google.common.collect.Maps;
//import  com.google.common.util.concurrent.ThreadFactoryBuilder;
//import  com.weiming.scav2.common.enums.ClientTypeEnum;
//import  com.weiming.scav2.common.utils.ScaNoBodyNameUtil;
//import  com.weiming.scav2.domain.security.CurrentUser;
//import  com.weiming.scav2.domain.security.LoginUser;
//import  com.weiming.scav2.infrastructure.person.PersonInfoRemoteService;
//import  com.weiming.scav2.infrastructure.person.rsp.PersonInfoRsp;
//import  com.weiming.scav2.infrastructure.remote.ExportRemoteService;
//import  com.weiming.scav2.infrastructure.remote.tags.*;
//import  com.weiming.web.protocal.RespStatus;
//import  com.weiming.web.protocal.ResponseData;
//import  io.swagger.annotations.Api;
//import  io.swagger.annotations.ApiOperation;
//import  lombok.extern.slf4j.Slf4j;
//import  org.apache.commons.lang.StringUtils;
//import  org.apache.poi.ss.formula.functions.T;
//import  org.apache.poi.ss.usermodel.*;
//import  org.apache.poi.xssf.usermodel.XSSFRow;
//import  org.apache.poi.xssf.usermodel.XSSFSheet;
//import  org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import  org.springframework.beans.factory.annotation.Autowired;
//import  org.springframework.http.HttpHeaders;
//import  org.springframework.http.HttpStatus;
//import  org.springframework.http.MediaType;
//import  org.springframework.http.ResponseEntity;
//import  org.springframework.util.CollectionUtils;
//import  org.springframework.web.bind.annotation.*;
//import  org.springframework.web.multipart.MultipartFile;
//import  springfox.documentation.annotations.ApiIgnore;
//
//import  javax.servlet.http.HttpServletResponse;
//import  javax.validation.Valid;
//import  java.io.ByteArrayOutputStream;
//import  java.io.IOException;
//import  java.net.URLEncoder;
//import  java.util.*;
//import  java.util.concurrent.*;
//
///**
// *  @author:  tony
// *  @date:  2019/7/13  10:00
// *  @description
// */
//@Slf4j
//@Api(tags  =  "租户标签管理")
//@RestController
//@RequestMapping("/v1/tags")
//public  class  TagsController  {
//
//    @Autowired
//    private  TagsRemoteService  tagsRemoteService;
//    @Autowired
//    private  ExportRemoteService  exportRemoteService;
//    @Autowired
//    private  PersonInfoRemoteService  personInfoRemoteService;
//
//
//    private  static  final  String  ALL_PROVINCE_CODE  =  "D100000";
//    private  static  final  String  TITLE_ID  =  "客户ID";
//    private  static  final  Integer  FILE_MAX_LENGTH  =  10000;
//
//
//    @PostMapping("/search")
//    @ApiOperation(value  =  "租户标签列表分页查询")
//    public  ResponseData  searchClientTags(@RequestBody  @Valid  TagsQueryReq  tagsQueryReq,  @ApiIgnore  @CurrentUser  LoginUser  authUserDetails)  {
//        Long  tenantId  =  authUserDetails.getTenantId();
//        TagsQueryCriteria  tagsQueryCriteria  =  new  TagsQueryCriteria();
//        tagsQueryCriteria.setClientCityCode(tagsQueryReq.getCityCode());
//        tagsQueryCriteria.setClientDistrictCode(tagsQueryReq.getDistrictCode());
//        if  (!ALL_PROVINCE_CODE.equals(tagsQueryReq.getProvinceCode()))  {
//            tagsQueryCriteria.setClientProvinceCode(tagsQueryReq.ge tProvinceCode());
//        }
//        tagsQueryCriteria.setClientName(tagsQueryReq.getClientName());
//        tagsQueryCriteria.setClientType(tagsQueryReq.getClientType());
//        tagsQueryCriteria.setParentClientName(tagsQueryReq.getParentClientName());
//        tagsQueryCriteria.setTags(tagsQueryReq.getTags());
//        tagsQueryCriteria.setDisplayEmpty(tagsQueryReq.isDisplayEmpty());
//        ResponseData<PageSerializable<TagRemoteListResp>> data = tagsRemoteService.searchClientTags(tenantId, tagsQueryReq.getPage(), tagsQueryReq.getLimit(), tagsQueryCriteria);
//
//        List<TagListResp> tagListRespslist = new ArrayList<>();
//        if (data != null && data.getData() != null && data.getData().getList() != null) {
//            List<TagRemoteListResp> tagClientList = data.getData().getList();
//            TagListResp tagClientResp = null;
//            Map<String, String> tagCodeNameMap = getTagDimensionMapVN(tenantId);
//            for (TagRemoteListResp tagClient : tagClientList) {
//                tagClientResp = new TagListResp();
//                tagClientResp.setId(tagClient.getId());
//                tagClientResp.setClientName(tagClient.getClientName());
//                String clientType = tagClient.getClientType();
//                Integer type = Objects.isNull(clientType) ? null : Integer.valueOf(clientType);
//                tagClientResp.setClientType(ClientTypeEnum.getTypeNameByType(type));
//                tagClientResp.setHeadquartersName(tagClient.getParentClientName());
//                List<Map> clientTagsList = new ArrayList<>();
//                Map<String, String> tagTypeValueMap = tagClient.getTags();
//                if (tagTypeValueMap != null) {
//                    for (String tagType : tagTypeValueMap.keySet()) {
//                        Map<String, String> map = new HashMap<>();
//                        String tagValue = tagTypeValueMap.getOrDefault(tagType, "");
//                        if (StringUtils.isNotBlank(tagValue)) {
//                            if ("operatorTag".equals(tagType) || "branchManagerTag".equals(tagType)) {
//                                if ("p000000000000000000000000".equals(tagValue)) {
//                                    String noBodyName = ScaNoBodyNameUtil.getNoBodyName(tagType, tenantId);
//                                    map.put("tagLabel", noBodyName);
//                                } else {
//                                    ResponseData<PersonInfoRsp> responseData = personInfoRemoteService.getPersonInfo(tagTypeValueMap.get(tagType));
//                                    if (responseData != null && responseData.getData() != null) {
//                                        map.put("tagLabel", responseData.getData().getRealName());
//                                    }
//                                }
//                            } else {
//                                map.put("tagLabel", tagCodeNameMap.get(tagValue));
//                            }
//                        }
//                        map.put("tagValue", tagValue);
//                        map.put("tagProperty", tagType);
//                        clientTagsList.add(map);
//                    }
//                    tagClientResp.setTags(clientTagsList);
//                }
//
//                tagListRespslist.add(tagClientResp);
//            }
//
//            PageSerializable<TagListResp> listRespPage = PageSerializable.of(tagListRespslist);
//            listRespPage.setTotal(data.getData().getTotal());
//            return ResponseData.ok(listRespPage);
//        }
//        return ResponseData.ok(null);
//
//    }
//
//    @GetMapping("/config")
//    @ApiOperation(value = "租户标签配置元数据")
//    public ResponseData getTenantTags(@ApiIgnore @CurrentUser LoginUser authUserDetails) {
//        Long tenantId = authUserDetails.getTenantId();
//        String tenantUuid = authUserDetails.getTenantUuid();
//        return tagsRemoteService.getTenantTags(tenantId, tenantUuid);
//    }
//
//    @PostMapping("/export")
//    @ApiOperation(value = "租户标签配置数据导出")
//    public ResponseData exportClientTags(@RequestBody @Valid TagsQueryReq tagsQueryReq, @ApiIgnore @CurrentUser LoginUser authUserDetails) {
//        Long tenantId = authUserDetails.getTenantId();
//        String userId = authUserDetails.getUuid();
//        TagsQueryCriteria tagsQueryCriteria = new TagsQueryCriteria();
//        tagsQueryCriteria.setDisplayEmpty(tagsQueryReq.isDisplayEmpty());
//        tagsQueryCriteria.setClientCityCode(tagsQueryReq.getCityCode());
//        tagsQueryCriteria.setClientDistrictCode(tagsQueryReq.getDistrictCode());
//        tagsQueryCriteria.setClientProvinceCode(tagsQueryReq.getProvinceCode());
//        tagsQueryCriteria.setClientName(tagsQueryReq.getClientName());
//        tagsQueryCriteria.setClientType(tagsQueryReq.getClientType());
//        tagsQueryCriteria.setParentClientName(tagsQueryReq.getParentClientName());
//
//        tagsQueryCriteria.setTags(tagsQueryReq.getTags());
//        exportRemoteService.exportTagsClientExport(tenantId, userId, tagsQueryCriteria);
//        return ResponseData.ok();
//    }
//
//    private ResponseEntity checkUploadFileProperties(Sheet sheet, List<TagRemoteConfigResp> configTagList) {
//// 获取最后一行, 计数从0开始
//        int lastRowNum = sheet.getLastRowNum();
//
//// 校验最大行数
//        if (lastRowNum > FILE_MAX_LENGTH) {
//            String reason = "文件最大只能支持" + FILE_MAX_LENGTH + "条,请重新上传";
//            String debugMsg = "文件超出" + FILE_MAX_LENGTH + "条";
//            ResponseData<T> responseData = ResponseData.build(RespStatus.COMMON_ERROR.code(), reason, debugMsg, null);
//            return ResponseEntity.ok(responseData);
//        }
//
//// 校验最小行数
//        if (lastRowNum < 1) {
//            String reason = "文件不能为空,请重新上传";
//            String debugMsg = "文件不能为空,请重新上传";
//            ResponseData<T> responseData = ResponseData.build(RespStatus.COMMON_ERROR.code(), reason, debugMsg, null);
//            return ResponseEntity.ok(responseData);
//        }
//
//// 校验表头
//        Map<String, Integer> headerMap = getHeaderIndex(sheet.getRow(0));
//        boolean titleCheckResultFlag = true;
//        if (headerMap.get(TITLE_ID) == null) {
//            titleCheckResultFlag = false;
//        }
//
//        for (TagRemoteConfigResp configTag : configTagList) {
//            if (!headerMap.containsKey(configTag.getName())) {
//                titleCheckResultFlag = false;
//                break;
//            }
//        }
//        if (!titleCheckResultFlag) {
//            String reason = "您使用的不是标准导入模板，请更换模板之后再导入数据。";
//            String debugMsg = "您使用的不是标准导入模板，请更换模板之后再导入数据。";
//            ResponseData<T> responseData = ResponseData.build(RespStatus.COMMON_ERROR.code(), reason, debugMsg, null);
//            return ResponseEntity.ok(responseData);
//        }
//        return null;
//    }
//
//    private void checkUploadFileContent(Sheet sheet, Long tenantId, String tenantUuid, List<String> checkFailRowList, Map<String, String> tagNvMap, List<TagRemoteConfigResp> configTagList, Short lastCellNum) {
//        int lastRowNum = sheet.getLastRowNum();
//        int pageSize = 5000;
//        int pageNum = lastRowNum % pageSize == 0 ? lastRowNum / pageSize : (lastRowNum / pageSize + 1);
//        ExecutorService executorService = new ThreadPoolExecutor(pageNum, FILE_MAX_LENGTH / pageSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("scav2-上传文件批量修改标签-校验文件内容-%d").build());
//        CountDownLatch countDownLatch = new CountDownLatch(pageNum);
//
//        for (int i = 0; i < pageNum; i++) {
//            int startIndex = i * pageSize;
//            if (i == 0) {
//                startIndex = i * pageSize + 1;
//            }
//            int endIndex = (i + 1) * pageSize > lastRowNum ? lastRowNum : ((i + 1) * pageSize);
//            Map<String, Integer> headerMap = getHeaderIndex(sheet.getRow(0));
//
//            TagUploadExcelWorker tagUploadExcelWorker = new TagUploadExcelWorker(tenantId, tenantUuid,
//                    countDownLatch, startIndex, endIndex, headerMap,
//                    tagNvMap, configTagList, sheet, checkFailRowList, lastCellNum);
//            tagUploadExcelWorker.setExportRemoteService(exportRemoteService);
//            tagUploadExcelWorker.setPersonInfoRemoteService(personInfoRemoteService);
//            tagUploadExcelWorker.setTagsRemoteService(tagsRemoteService);
//            executorService.submit(tagUploadExcelWorker);
//        }
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            log.error("使用多线程校验文件内容时，出现异常.", e);
//        }
//    }
//
//    @PostMapping("/upload")
//    @ApiOperation(value = "租户标签配置数据上传")
//    public ResponseEntity uploadClientTags(@RequestParam("file") MultipartFile file, @ApiIgnore @CurrentUser LoginUser authUserDetails) {
//        Long tenantId = authUserDetails.getTenantId();
//        String tenantUuid = authUserDetails.getTenantUuid();
//        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
//// 第一个Sheet页
//            Sheet sheet = workbook.getSheetAt(0);
//// 获取已经配置的tag
//            List<TagRemoteConfigResp> configTagList = getTagDimensionByTenantId(tenantId);
//// 对文件进行初步校验
//            ResponseEntity responseEntity = checkUploadFileProperties(sheet, configTagList);
//            if (Objects.nonNull(responseEntity)) {
//                return responseEntity;
//            }
//
//// 内容校验前准备
//            Short lastCellNum = sheet.getRow(0).getLastCellNum();
//            Cell firstHeadCell = sheet.getRow(0).createCell(lastCellNum);
//            firstHeadCell.setCellValue("错误原因");
//            List<String> checkFailRowList = new ArrayList<>();
////获取租户标签属性值
//            Map<String, String> tagNvMap = getTagDimensionMapNV(tenantId);
//
//// 对文件内容进行校验
//            checkUploadFileContent(sheet, tenantId, tenantUuid, checkFailRowList, tagNvMap, configTagList, lastCellNum);
//
//            if (CollectionUtils.isEmpty(checkFailRowList)) {
//                return ResponseEntity.ok(ResponseData.ok("处理成功"));
//            } else {
////错误文件下载
//                HttpHeaders headers = new HttpHeaders();
//                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//                headers.add("Content-Disposition", "attachment; filename=" + file.getName());
//                headers.add("Pragma", "no-cache");
//                headers.add("Expires", "0");
//                headers.add("Last-Modified", new Date().toString());
//                headers.add("ETag", String.valueOf(System.currentTimeMillis()));
//                headers.setContentDispositionFormData("attachment", file.getOriginalFilename());
//                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//                ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
//
//                workbook.write(outByteStream);
//                byte[] bytes = outByteStream.toByteArray();
//                return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
//            }
//        } catch (Exception e) {
//            log.error("文件上传,批量处理修改标签数据时,出现异常", e);
//            String reason = "文件处理失败,请重新上传";
//            String debugMsg = "文件处理失败,请重新上传";
//            ResponseData<T> responseData = ResponseData.build(RespStatus.COMMON_ERROR.code(), reason, debugMsg, null);
//            return ResponseEntity.ok(responseData);
//        }
//    }
//
//    @PostMapping("/edit")
//    @ApiOperation(value = "租户标签配置编辑保存")
//    public ResponseData editClientTags(@RequestBody @Valid TagsEditInfoReq tagsEditInfoReq, @ApiIgnore @CurrentUser LoginUser authUserDetails) {
//        Long tenantId = authUserDetails.getTenantId();
//        TagRemoteClientReq remoteClientReq = new TagRemoteClientReq();
//        remoteClientReq.setId(tagsEditInfoReq.getId());
//        remoteClientReq.setTags(tagsEditInfoReq.getTags());
//        tagsRemoteService.editClientTags(tenantId, remoteClientReq);
//        return ResponseData.ok("处理成功");
//    }
//
//    @GetMapping("/downloadTemplate")
//    @ApiOperation(value = "模板下载")
//    public void downloadTemplate(HttpServletResponse response, @ApiIgnore @CurrentUser LoginUser authUserDetails) {
//        XSSFWorkbook workbook = null;
//        try {
//            workbook = new XSSFWorkbook();
//// 文件的默认保存名
//            Long tenantId = authUserDetails.getTenantId();
//
////客户ID	客户名称	区域	总部名称	客户类型	客户归属	客户属性	商业标签	连锁标签	连锁品牌	销售类型	是否协议客户	省总	责任业务员
////客户ID	客户名称	区域	总部名称	客户类型	客户归属	客户属性	商业标签	连锁标签	连锁品牌	销售类型	是否协议客户	业务经经理	KA经理
//            ResponseData<List<TagRemoteConfigResp>> responseData = tagsRemoteService.getTagDimensionByTenantId(tenantId);
//
//            List<TagRemoteConfigResp> remoteConfigRespList = responseData.getData();
//            XSSFSheet sheet = workbook.createSheet("sheet");
//            int row = 0;
//            int col = 0;
//            XSSFRow row0 = sheet.createRow(row);
////客户ID	客户名称	区域	总部名称	客户类型	客户归属	客户属性	商业标签	连锁标签	连锁品牌	销售类型	是否协议客户	省总	责任业务员
//            row0.createCell(col++).setCellValue("客户ID");
//            row0.createCell(col++).setCellValue("客户名称");
//            row0.createCell(col++).setCellValue("区域");
//            row0.createCell(col++).setCellValue("总部名称");
//            row0.createCell(col++).setCellValue("客户类型");
//            for (TagRemoteConfigResp tagRemoteConfigResp : remoteConfigRespList) {
//                row0.createCell(col++).setCellValue(tagRemoteConfigResp.getName());
//            }
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            workbook.write(baos);
//            String fileName = "批量修改标签信息表.xlsx";
//
//// 设置输出的格式
//            response.reset();
//            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//            response.setHeader("Pragma", "no-cache");
//            response.setHeader("Cache-Control", "no-cache");
//            response.setDateHeader("Expires", 0);
//            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//
//            byte[] bytes = baos.toByteArray();
//            response.getOutputStream().write(bytes);
//
//            baos.close();
//        } catch (IOException e) {
//            log.error("下载批量导入标签模板时出现异常", e);
//        } finally {
////关闭流
//            try {
//                if (workbook != null) {
//                    workbook.close();
//                }
//            } catch (IOException e) {
//                log.error("下载批量导入标签模板时出现异常", e);
//            }
//        }
//    }
//
//    /**
//     * 标签维度转换为 名称-code 形式
//     *
//     * @param tenantId 租户id
//     * @return
//     */
//    private Map<String, String> getTagDimensionMapNV(long tenantId) {
//
//        ResponseData<Map<String, String>> responseData = tagsRemoteService.getTagMapNV(tenantId);
//        return responseData.getData();
//    }
//
//    /**
//     * 标签维度转换为 code-名称 形式
//     *
//     * @param tenantId 租户id
//     * @return
//     */
//    private Map<String, String> getTagDimensionMapVN(long tenantId) {
//        ResponseData<Map<String, String>> responseData = tagsRemoteService.geTagtMapVN(tenantId);
//        return responseData.getData();
//    }
//
//    /**
//     * @param tenantId
//     * @return
//     */
//    private List<TagRemoteConfigResp> getTagDimensionByTenantId(long tenantId) {
//        ResponseData<List<TagRemoteConfigResp>> tagConfigResp = tagsRemoteService.getTagDimensionByTenantId(tenantId);
//        List<TagRemoteConfigResp> tagRemoteConfigResps = tagConfigResp.getData();
//        return tagRemoteConfigResps;
//    }
//
//
//    private Map<String, Integer> getHeaderIndex(Row headerRow) {
//        Map<String, Integer> excelColumnNames = Maps.newHashMap();
//        int index = 0;
//        for (Cell cell : headerRow) {
//            excelColumnNames.put(cell.getStringCellValue(), index++);
//        }
//        return excelColumnNames;
//    }
//}