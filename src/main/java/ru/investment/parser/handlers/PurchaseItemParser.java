package ru.investment.parser.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.investment.exceptions.root.ParsingException;
import ru.investment.parser.handlers.markers.IParseRoot;

@SuppressWarnings("JsonStandardCompliance")
@Slf4j
@Component
@RequiredArgsConstructor
@Scope("prototype")
public class PurchaseItemParser implements IParseRoot {

    public void parseItem(String sourceUrl) throws ParsingException {
//        final ParserThread parserThread = ((ParserThread) Thread.currentThread());
//        final PurchasePage purchasePage =
//            ((PurchasePage) parserThread.get(ParserThread.ENVIRONMENTS.PURCHASE_PAGE));
//        final String customerUrl = purchasePage.getCustomerUrl();
//
//        Selenide.sleep(props.getEachStateSleepMs());
//
//        try {
//            try {
//                header = System.currentTimeMillis();
//                headerParseHandler.parseFrom(purchase);
//                header = (System.currentTimeMillis() - header) / 1000;
//            } catch (Throwable t) {
//                log.error("Ошибка при парсинге хэдера: " + t);
//                throw t;
//            }
//
//            try {
//                if (purchase.getId() == null) {
//                    // сохраняем закупку в БД для получения её id:
//                    purchaseService.saveAndFlush(purchase);
//                }
//            } catch (Throwable t) {
//                log.error("Сохранение закупки с хэдером: " + t);
//                throw t;
//            }
//
//            parseBody(purchase, xmlRoot);
//        } catch (ParsingException pe) {
//            log.error("Ошибка шаблона: " + pe);
//            throw pe;
//        } catch (Throwable t) {
//            if (t.getCause() instanceof org.openqa.selenium.TimeoutException) {
//                log.error("Парсинг выполняется слишком долго: " + t.getMessage(), t);
//            } else {
//                log.error("Ошибка парсера: " + t.getMessage(), t);
//            }
//            throw t;
//        } finally {
//            BrowserUtils.closeWindow();
//        }
//
//        try {
//            // сохраняем результат в БД:
//            purchaseService.saveAndFlush(purchase);
//            Selenide.sleep(props.getEachStateSleepMs());
//
//            purchasePageService.saveAndFlush(
//                purchasePage.status(PurchaseStates.PARSED_SUCCESSFULLY.name()).lastParsedDate(UniversalDateParser.now())
//            );
//
//            log.info("Поток " + Thread.currentThread().getId() + " успешно завершил парсинг закупки " + purchase.getUrl());
//            taskAccomplishedUpdate(parserThread);
//        } catch (Throwable t) {
//            throw new ParsingException("Ошибка сохранения закупки после парсинга: " + t.getMessage());
//        }
    }

//    private JsonNode getCardXml(Purchase purchase) {
//        try {
//            URL url;
//            HttpURLConnection connection;
//            StringBuilder sb = new StringBuilder();
//
//            if (purchase.getLaw().equals(PurchaseLaw.FZ44.value())) {
//                url = new URL("https://zakupki.gov.ru/epz/order/notice/printForm/viewXml.html?regNumber=" + purchase.getNumber());
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String inputLine;
//                    while ((inputLine = in.readLine()) != null) {
//                        sb.append(inputLine);
//                    }
//                }
//            } else if (purchase.getLaw().equals(PurchaseLaw.FZ223.value())) {
//                if ($(".subscribeRss__link").exists()) {
//                    String noticeId = $(".subscribeRss__link").attr("href").split("id=")[1];
//                    url = new URL("https://zakupki.gov.ru/epz/order/notice/printForm/viewXml.html?noticeId=" + noticeId);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//
//                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                        String inputLine;
//                        while ((inputLine = in.readLine()) != null) {
//                            sb.append(inputLine);
//                        }
//                    } catch (FileNotFoundException fnf) {
//                        if (
//                            $(".registry-entry__header-top__icon").exists() &&
//                            $(".registry-entry__header-top__icon").$$x(".//a//img").size() > 0
//                        ) {
//                            noticeId =
//                                $(".registry-entry__header-top__icon")
//                                    .$$x(".//a//img")
//                                    .filter(Condition.cssClass("m-0"))
//                                    .get(0)
//                                    .parent()
//                                    .attr("href");
//
//                            // todo: бывает вместо номера проскакивает полный html и дублируется в url. Исправить или оставить как есть, через костыль.
//                            if (noticeId.startsWith("http")) {
//                                url = new URL(noticeId + "#tabs-2");
//                            } else {
//                                url =
//                                    new URL("https://zakupki.gov.ru/223/purchase/public/print-form/show.html?pfid=" + noticeId + "#tabs-2");
//                            }
//
//                            connection = (HttpURLConnection) url.openConnection(); // HttpURLConnection
//                            connection.setRequestMethod("GET");
//                            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                                String inputLine;
//                                while ((inputLine = in.readLine()) != null) {
//                                    sb.append(inputLine.trim()); // StringBuilder sb = new StringBuilder(); sb.toString(); + #tabs-2
//                                }
//                            }
//                            connection.disconnect();
//                        } else {
//                            throw fnf;
//                        }
//                    }
//                }
//            } else {
//                log.warn("Получение XML не прописано для этого закона: " + purchase.getLaw());
//            }
//
//            // если xml зашит в html:
//            String result = sb.toString();
//            if (result.contains("</div>")) {
//                result = result.substring(result.indexOf("?xml")).split(";</div>")[0];
//                result = result.replace("&#034;", "\"").replace("&gt;", ">").replace("&gt", ">").replace("&lt;", "<");
//                result = ("<").concat(result);
//            }
//
//            // work with xml:
//            return new XmlMapper().readTree(result); // id="tabs-2" -> </div>
//        } catch (Throwable t) {
//            log.error("Ошибка XML: " + t.getMessage()); // TODO: выяснить откуда берутся проблемы с чтением xml и устранить их.
//            return null;
//        }
//    }

//    private void parseBody(Purchase purchase, JsonNode xmlRoot) throws ParsingException {
//        easy = System.currentTimeMillis();
//        ElementsCollection colData = $$(".col-9");
//        parseMemberships(colData, purchase);
//        parseBankingSupport(purchase);
//        parseOtherData(colData, purchase);
//        easy = (System.currentTimeMillis() - easy) / 1000;
//
//        provides = System.currentTimeMillis();
//        providesParserHandler.parseFrom(purchase);
//        provides = (System.currentTimeMillis() - provides) / 1000;
//
//        trdinf = System.currentTimeMillis();
//        if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.FZ44.value())) {
//            if (!$(".blockInfo").exists()) {
//                log.info("Блок tradeInfo 'Информация об объекте закупки' не обнаружен. Закупка №" + purchase.getNumber());
//                return;
//            }
//            tradeInfoParserHandler.parseFrom(purchase, xmlRoot);
//        }
//        trdinf = (System.currentTimeMillis() - trdinf) / 1000;
//
//        funsrc = System.currentTimeMillis();
//        fundingSourceHandler.parseFrom(purchase);
//        funsrc = (System.currentTimeMillis() - funsrc) / 1000;
//    }

//    private void otherTabsGrabStage(Purchase purchase) throws ParsingException {
//        // парсим Окпд2 (у 223 закона в отдельной вкладке):
//        if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.FZ223.value())) {
//            parseOKPD2Classification(purchase);
//        }
//
//        // парсим EventLogs:
//        eventLogsHandler.parseFrom(purchase);
//        // TODO уточнить, нужно ли вообще:
//        // purchase.setCustomerRequirements(customerRequirementsParseHandler.parseFrom($$(".collapseInfo"), purchase));
//    }

//    private void parseOKPD2Classification(Purchase purchase) {
//        ElementsCollection lotsBlock = $$(".tabsNav__item").filter(Condition.text("СПИСОК ЛОТОВ"));
//        if (lotsBlock.size() == 0) {
//            log.warn("Блок списка лотов отсутствует или ошибка шаблона поиска. Закупка №" + purchase.getNumber());
//            return;
//        }
//
//        if (lotsBlock.size() == 1) {
//            log.debug("Try to switch to OKPD2 tab...");
//            lotsBlock.get(0).click();
//            Selenide.sleep(props.getTabClickSleepMs());
//
//            SelenideElement okpdField = $$(".card-common .table").filter(Condition.not(Condition.empty)).get(0).$x("tbody/tr[1]/td[4]");
//            if (okpdField.exists() && !okpdField.text().isBlank()) {
//                purchase.setLotInfoOkpd(okpdField.text());
//            } else {
//                log.debug("OKPD2 tab not found or switch was failed.");
//            }
//        } else {
//            log.warn("Блок имеет размер " + lotsBlock.size() + " что не соответствует шаблону поиска. Закупка №" + purchase.getNumber());
//        }
//    }

//    private void parseMemberships(ElementsCollection colData, Purchase purchase) throws ParsingException {
//        ElementsCollection requirementsBlock = colData.filter(Condition.text("Требования к участникам закупки")); // 223FZ
//        if (requirementsBlock.size() == 0) {
//            requirementsBlock = $$(".container .col").filter(Condition.text("Преимущества, требования к участникам")); // 44FZ
//            if (requirementsBlock.size() == 0) {
//                log.warn("Данные о преимуществах, требованиях к участникам закупки не найдены. Закупка №" + purchase.getNumber());
//                return;
//            }
//        }
//        // todo: здесь бывает кнопка 'Показать всё'! Доработать метод.
//        if (requirementsBlock.size() == 1) {
//            if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.FZ44.value())) {
//                ElementsCollection requirementsFields = requirementsBlock.get(0).$$(".blockInfo__section");
//
//                if (requirementsFields.size() != 0) {
//                    purchase.setMembershipRequirements(
//                        requirementsFields.filter(Condition.text("Требования к участникам")).get(0).$(".section__info").text()
//                    );
//
//                    purchase.setMembershipBenefits(
//                        requirementsFields.filter(Condition.text("Преимущества")).get(0).$(".section__info").text()
//                    );
//
//                    ElementsCollection requipBlockData = requirementsFields.filter(Condition.text("Ограничения и запреты"));
//                    if (requipBlockData.size() != 0) {
//                        purchase.setMembershipRestrictions(requipBlockData.get(0).$(".section__info").text());
//
//                        SelenideElement extRequirements = requipBlockData.get(0).$(".section__info .section__title");
//                        if (extRequirements.exists() && extRequirements.text().trim().equalsIgnoreCase("Дополнительные требования")) {
//                            purchase.setMembershipRequirementsExtended(requirementsBlock.get(0).$(".npa-card").text());
//                        }
//                    }
//                }
//            } else if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.FZ223.value())) {
//                ElementsCollection requirementsFields = requirementsBlock.get(0).$x("..").$$(".col-9");
//                if (requirementsFields.size() == 2) {
//                    purchase.setMembershipRequirementsExtended(requirementsFields.get(1).text());
//                } else {
//                    throw new NotEnoughDataException(
//                        "Неожиданный размер блока 'Требования и ограничения' закупки № " + purchase.getNumber()
//                    );
//                }
//            } else if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.PPRF615.value())) {
//                throw new ParsingException("TODO: 615 law is here, it`s not seem correct (требуется обработать редкий вариант)");
//            } else {
//                throw new NotEnoughDataException("Необработанный закон в методе 'parseMemberships' закупки № " + purchase.getNumber());
//            }
//        } else {
//            log.warn(
//                "Непредвиденный размер данных! Блок имеет размер " + requirementsBlock.size() + ", что не соответствует шаблону поиска!"
//            );
//        }
//    }

//    private void parseBankingSupport(Purchase purchase) throws ParsingException {
//        // заранее ставим false для упреждения null:
//        purchase.setIsBankingOrTreasurySupportNeeded(false);
//        if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.FZ223.value())) {
//            // у 223-х не должно быть такого блока вообще.
//            return;
//        }
//
//        ElementsCollection bankBlock;
//        if (purchase.getLaw().equalsIgnoreCase(PurchaseLaw.FZ44.value())) {
//            bankBlock =
//                $$(".custReqNoticeTable .container")
//                    .filter(Condition.text("Информация о банковском и (или) казначейском сопровождении контракта")); // 44FZ
//            if (bankBlock.size() == 0 && $$(".container .col").size() != 0) {
//                bankBlock =
//                    $$(".container .col").filter(Condition.text("Информация о банковском и (или) казначейском сопровождении контракта")); // 44FZ второй вариант
//                if (bankBlock.size() != 0) {
//                    String bankText = bankBlock.get(0).$(".section__info").text().trim();
//                    purchase.setIsBankingOrTreasurySupportNeeded(
//                        !bankText.equalsIgnoreCase("Банковское или казначейское сопровождение контракта не требуется")
//                    );
//                }
//            } else {
//                throw new NoDataAvailableException("Не удалось найти поле 'bankingOrTreasurySupportNeeded'");
//            }
//        } else {
//            log.warn(
//                "У закупки с законом " +
//                purchase.getLaw() +
//                " внезапно объявился блок информации о банковском сопровождении или ошибка шаблона. Закупка №" +
//                purchase.getNumber()
//            );
//        }
//    }

//    private void parseOtherData(ElementsCollection dataCollection, Purchase purchase) throws ParsingException {
//        // заранее ставим false для упреждения null:
//        purchase.setIsMedicalProduct(false);
//        purchase.setIsNecessaryForLifeSupport(false);
//        purchase.setIsQualityGuaranteeRequires(false);
//
//        if (dataCollection.size() <= 1) {
//            dataCollection = $$(".blockInfo__section"); // 44FZ
//        }
//
//        String key, value = null;
//        for (SelenideElement dataField : dataCollection.filter(Condition.not(Condition.empty))) {
//            if (dataField.text().isEmpty() || (!dataField.$(".section__title").exists() && !dataField.$(".common-text__title").exists())) {
//                continue; // такие данные обрабатываются в других методах.
//            }
//
//            try {
//                if (dataField.$(".section__title").exists()) {
//                    key = dataField.$(".section__title").text(); // 44FZ
//                } else if (dataField.$(".common-text__title").exists()) {
//                    key = dataField.$(".common-text__title").text();
//                } else {
//                    throw new NoElementAvailableException("Необработанный вариант поля key в закупке № {}", purchase.getNumber());
//                }
//                if (dataField.$(".section__info").exists()) {
//                    value = dataField.$(".section__info").text(); // 44FZ
//                } else if (dataField.$(".common-text__value").exists()) {
//                    value = dataField.$(".common-text__value").text(); // 223FZ
//                } else if (dataField.$(".tableBlock").exists()) {
//                    Optional<SelenideElement> valueTest = dataField
//                        .$(".tableBlock")
//                        .$$x("*//tr")
//                        .asFixedIterable()
//                        .stream()
//                        .skip(1)
//                        .findFirst(); // 223FZ
//                    if (valueTest.isPresent()) {
//                        value = valueTest.get().text();
//                    }
//                } else {
//                    throw new NoElementAvailableException("Необработанный вариант поля value в закупке № {}", purchase.getNumber());
//                }
//            } catch (Throwable t) {
//                throw new NoElementAvailableException(
//                    "Необходимо доработать поиск нужных элементов. Ошибка в закупке № {}",
//                    purchase.getNumber(),
//                    t
//                );
//            }
//
//            if (value == null) {
//                value = "[NA]";
//            }
//            switch (key) {
//                case "Сведения о связи с позицией плана-графика":
//                    purchase.setPlanShipmentDetail(value);
//                    if (dataField.$(".section__info").exists() && dataField.$(".section__info").$x("a").exists()) {
//                        purchase.setPlanShipmentDetailUrl(dataField.$(".section__info").$x("a").attr("href"));
//                    } else if (!dataField.$(".section__info").text().equalsIgnoreCase("Связь с позицией плана-графика не установлена")) {
//                        log.warn("Поле 'Сведения о связи с позицией плана-графика' обнаружено, но url в тексте не найден!");
//                    }
//                    break;
//                case "Способ определения поставщика (подрядчика, исполнителя)":
//                case "Способ определения поставщика (подрядчика, исполнителя, подрядной организации)":
//                    purchase.setVendorDefinition(value);
//                    break;
//                case "Дата размещения извещения":
//                    purchase.setNoticePostingDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Дата размещения текущей редакции извещения":
//                    purchase.setCurrentNoticeDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Редакция":
//                    purchase.setRedactionCode(UniversalNumberParser.parseInt(value));
//                    break;
//                case "Время проведения электронного аукциона":
//                    buildNoticeAuctionDate(null, value, purchase);
//                    break;
//                case "Дата проведения электронного аукциона":
//                    buildNoticeAuctionDate(value, null, purchase);
//                    break;
//                case "Дата проведения процедуры подачи предложений о цене контракта":
//                case "Дата проведения процедуры подачи предложений о цене контракта либо о сумме цен единиц товара, работы, услуги":
//                    purchase.setNoticeAuctionDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Место подведения итогов":
//                    purchase.setDebriefingAddress(value);
//                    break;
//                case "Дата подведения итогов":
//                    purchase.setDebriefingDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Дата подведения итогов определения поставщика (подрядчика, исполнителя)":
//                case "Дата окончания срока рассмотрения заявок на участие в электронном аукционе":
//                    purchase.setNoticeDefinitDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Способ осуществления закупки":
//                    purchase.setProcurementMethod(value);
//                    break;
//                case "Дата принятия решения о внесении изменений":
//                    purchase.setDecisionChangesDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Причина внесения изменений":
//                    purchase.setChangeReason(value);
//                    break;
//                case "Дата и время начала срока подачи заявок":
//                case "Дата начала срока подачи заявок":
//                    purchase.setNoticeStartDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Порядок подачи заявок":
//                    purchase.setNoticeSendProcedure(value);
//                    break;
//                case "Дата рассмотрения заявок":
//                case "Дата и время окончания срока подачи заявок":
//                case "Дата и время окончания срока подачи заявок на участие в электронном аукционе":
//                case "Дата и время окончания срока подачи заявок (по местному времени заказчика)":
//                    purchase.setNoticeDebriefingDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Место подачи заявок":
//                    purchase.setNoticeSendAddress(value);
//                    break;
//                case "Порядок подведения итогов":
//                    purchase.setDebriefingProcedure(value);
//                    break;
//                case "Наименование электронной площадки в информационно-телекоммуникационной сети «Интернет»":
//                case "Наименование электронной площадки в информационно-телекоммуникационной сети \"Интернет\"":
//                case "Наименование электронной площадки в сети «Интернет»":
//                    purchase.setElectronicPlatformName(value);
//                    break;
//                case "Адрес электронной площадки в информационно-телекоммуникационной сети «Интернет»":
//                case "Адрес электронной площадки в информационно-телекоммуникационной сети \"Интернет\"":
//                case "Сайт оператора электронной площадки в сети «Интернет»":
//                    purchase.setElectronicPlatformUrl(value);
//                    break;
//                case "Реестровый номер извещения":
//                    purchase.setNoticeRegistryNumber(value);
//                    break;
//                case "Срок предоставления":
//                    purchase.setDocumentationSubmissionDeadline(value);
//                    break;
//                case "Место предоставления":
//                    purchase.setDocumentationSubmissionPlace(value);
//                    break;
//                case "Порядок предоставления":
//                    purchase.setDocumentationSubmissionProcedure(value);
//                    break;
//                case "Официальный сайт ЕИС, на котором размещена документация":
//                    purchase.setDocumentationSubmissionSite(value);
//                    break;
//                case "Внесение платы за предоставление конкурсной документации":
//                    purchase.setDocumentationSubmissionPay(value);
//                    break;
//                case "Закупка лекарственного препарата":
//                    purchase.isMedicalProduct(value.equalsIgnoreCase("да"));
//                    break;
//                case "Предметом контракта является поставка товара, необходимого для нормального жизнеобеспечения в случаях, указанных в ч. 9 ст. 37 Закона 44-ФЗ":
//                    purchase.setIsNecessaryForLifeSupport(value.equalsIgnoreCase("да"));
//                    break;
//                case "Виды работ в соответствии с ч.1 ст. 166 Жилищного кодекса":
//                    purchase.setTypeOfWork(value);
//                    break;
//                case "Контактное лицо":
//                case "Ответственное должностное лицо":
//                    purchase.setCiContactPerson(value);
//                    break;
//                case "Адрес электронной почты":
//                    purchase.setCiEmail(value);
//                    break;
//                case "Номер телефона":
//                case "Контактный телефон":
//                case "Номер контактного телефона":
//                    purchase.setCiPhone(value);
//                    break;
//                case "Адрес":
//                case "Почтовый адрес":
//                    purchase.setCiPostAddress(value);
//                    break;
//                case "Место нахождения":
//                    purchase.setCiLocationAddress(value);
//                    break;
//                case "Факс":
//                    purchase.setCiFax(value);
//                    break;
//                case "Регион":
//                    purchase.setCiWorldRegion(value);
//                    break;
//                case "Дополнительная информация":
//                    purchase.setAdditionalInfo(value);
//                    break;
//                case "Дополнительная контактная информация":
//                    purchase.setCiExtendsInfo(value);
//                    break;
//                case "Размещение осуществляет":
//                    purchase.setCiAccomCarriesType(value);
//                    break;
//                case "Наименование организации":
//                case "Организация, осуществляющая размещение":
//                    purchase.setCiAccomCarriesName(value);
//                    break;
//                case "Этап закупки":
//                    if (purchase.getStage() == null) {
//                        purchase.setStage(value);
//                    }
//                    break;
//                case "Валюта":
//                    if (purchase.getCurrency() == null) {
//                        purchase.setCurrency(value);
//                    }
//                    break;
//                case "Начальная (максимальная) цена контракта":
//                case "Начальная (максимальная) цена контрактов":
//                case "Начальная (максимальная) цена договора, рублей":
//                    if (purchase.getStartPrice() == null) {
//                        purchase.setStartPrice(UniversalNumberParser.parseDouble(value.split(" в ")[0]));
//                    }
//                    break;
//                case "Максимальное значение цены контракта":
//                case "Максимальное значение цены контрактов":
//                    if (purchase.getMaxPrice() == null) {
//                        purchase.setMaxPrice(UniversalNumberParser.parseDouble(value));
//                    }
//                    break;
//                case "Размер аванса, %":
//                    purchase.setAdvancePaymentPercent(value);
//                    break;
//                case "Идентификационный код закупки (ИКЗ)":
//                    purchase.setIdentificationCode(value);
//                    break;
//                case "Наименование объекта закупки":
//                    break;
//                case "Преимущества":
//                    if (purchase.getMembershipBenefits() == null) {
//                        purchase.setMembershipBenefits(value);
//                    }
//                case "Требования к участникам":
//                    if (purchase.getMembershipRequirements() == null) {
//                        purchase.setMembershipRequirements(value);
//                    }
//                case "Ограничения и запреты":
//                    if (purchase.getMembershipRestrictions() == null) {
//                        purchase.setMembershipRestrictions(value);
//                    }
//                case "Дополнительные требования":
//                    if (purchase.getMembershipRequirementsExtended() == null) {
//                        purchase.setMembershipRequirementsExtended(value);
//                    }
//                    break;
//                case "Место поставки товара, выполнения работы или оказания услуги":
//                case "Место выполнения работ и (или) оказания услуг":
//                    purchase.setWorkProvisionPlace(value);
//                    break;
//                case "Право заключения контрактов с несколькими участниками закупки в случаях, указанных в части 10 статьи 34 Федерального закона 44-ФЗ":
//                    purchase.setFederalLawConcludeSeveralRight(value);
//                    break;
//                case "Срок исполнения контракта, срок исполнения и цена отдельных этапов исполнения контракта":
//                case "Сроки выполнения работ и (или) оказания услуг":
//                    purchase.setTurnaroundTime(value);
//                    break;
//                case "Номер типовых условий контракта":
//                    purchase.setContractConditionsNumber(value);
//                    break;
//                case "Условия оплаты выполненных работ и (или) оказанных услуг":
//                    purchase.setPayConditions(value);
//                    break;
//                case "Предусмотрена возможность одностороннего отказа от исполнения контракта в соответствии со ст. 95 Закона № 44-ФЗ":
//                    purchase.setContractUnilateralRefusalPossibility(value.equalsIgnoreCase("да"));
//                    break;
//                case "Предоставление документации":
//                    // TODO требуется ли реализовать? // dataCollection.filter(Condition.text("Предоставление документации"))
//                    break;
//                case "Информация о требованиях к гарантийному обслуживанию товара":
//                    purchase.setRequirementsProductWarrantyInfo(value);
//                    break;
//                case "Требования к гарантии производителя товара":
//                    purchase.setManufactureWarrantyRequirements(value);
//                    break;
//                case "Срок, на который предоставляется гарантия":
//                case "Срок, на который предоставляется гарантия и (или) требования к объему предоставления гарантий качества товара, работы, услуги":
//                    purchase.setGuaranteeProvidedPeriod(value);
//                    break;
//                case "Требуется гарантия качества товара, работы, услуги":
//                    purchase.setIsQualityGuaranteeRequires(value.equalsIgnoreCase("да"));
//                    break;
//                case "Дата рассмотрения первых частей заявок":
//                case "Дата рассмотрения первых частей заявок (по местному времени заказчика)":
//                case "Дата окончания срока рассмотрения и оценки первых частей заявок":
//                    purchase.setNoticeFirstPartDefinitDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Дата рассмотрения вторых частей заявок":
//                case "Дата рассмотрения вторых частей заявок (по местному времени заказчика)":
//                case "Дата окончания срока рассмотрения и оценки вторых частей заявок":
//                    purchase.setNoticeSecondPartDefinitDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Информация об особенностях осуществления закупки в соответствии с ч. 4-6 ст. 15 Закона № 44-ФЗ":
//                    purchase.setProcurementSpecificsInfo(value);
//                    break;
//                case "Специализированная организация":
//                    purchase.setCiSpecializedOrganization(value);
//                    break;
//                case "Шаг аукциона":
//                    purchase.setAuctionStep(value);
//                    break;
//                case "Порядок рассмотрения первых частей заявок":
//                    purchase.setFirstPartsConsiderationOrder(value);
//                    break;
//                case "Порядок рассмотрения вторых частей заявок":
//                    purchase.setSecondPartsConsiderationOrder(value);
//                    break;
//                case "Порядок рассмотрения заявок":
//                    purchase.setNoticeDefinitProcedure(value);
//                    break;
//                case "Место рассмотрения заявок":
//                    purchase.setNoticeDefinitAddress(value);
//                    break;
//                case "Формула цены контракта":
//                    purchase.setContractPriceFormula(value);
//                    break;
//                case "Место рассмотрения первых частей заявок":
//                    purchase.setNoticeFirstPartDefinitPlace(value);
//                    break;
//                case "Место рассмотрения вторых частей заявок":
//                    purchase.setNoticeSecondPartDefinitPlace(value);
//                    break;
//                case "Дата начала срока подачи ценовых предложений":
//                    purchase.setNoticePriceStartDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Дата и время окончания срока подачи ценовых предложений":
//                case "Дата и время окончания срока подачи ценовых предложений (по местному времени заказчика)":
//                    purchase.setNoticePriceFinishDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Порядок подачи ценовых предложений":
//                    purchase.setNoticeSubmitQuotateProcedure(value);
//                    break;
//                case "Указать информацию о сроках подачи ценовых предложений":
//                    purchase.setNoticeSendDateInfo(value);
//                    break;
//                case "Время начала срока подачи ценовых предложений":
//                    purchase.setNoticeSubmitQuotateStartDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Время начала срока подачи ценовых предложений (по местному времени заказчика)":
//                    if (value.split(":").length > 2 || value.trim().contains(" ")) {
//                        purchase.setNoticeSubmitQuotateStartDate(UniversalDateParser.parseDateTime(value));
//                    }
//                    break;
//                case "Дата проведения сопоставления ценовых предложений":
//                    purchase.setNoticePriceComparisonDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Порядок проведения сопоставления ценовых предложений":
//                    purchase.setNoticeComparingPriceOfferProcedure(value);
//                    break;
//                case "Дата и время окончания срока подачи дополнительных ценовых предложений":
//                    purchase.setNoticeAdditionPriceFinishDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Дата начала срока подачи дополнительных ценовых предложений":
//                    purchase.setNoticeAdditionPriceStartDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Указать информацию о сроках подачи дополнительных ценовых предложений":
//                    purchase.setNoticeAdditionSendDateInfo(value);
//                    break;
//                case "Время начала срока подачи дополнительных ценовых предложений":
//                case "Время начала срока подачи дополнительных ценовых предложений (по местному времени заказчика)":
//                    purchase.setNoticeAdditionSubmitQuotateStartDate(UniversalDateParser.parseDateTime(value)); // 15:00 is correct?
//                    break;
//                case "Порядок подачи дополнительных ценовых предложений":
//                    purchase.setNoticeAdditionSubmitQuotateProcedure(value);
//                    break;
//                case "Дата проведения сопоставления дополнительных ценовых предложений":
//                    purchase.setNoticeAdditionPriceComparisonDate(UniversalDateParser.parseDateTime(value));
//                    break;
//                case "Порядок проведения сопоставления дополнительных ценовых предложений":
//                    purchase.setNoticeAdditionComparingPriceOfferProcedure(value);
//                    break;
//                case "Срок и порядок внесения платы за документацию":
//                    purchase.setDocumentationSubmissionInfo(value);
//                    break;
//                case "Тип казначейского сопровождения контракта":
//                    purchase.setBankingTreasurySupport(value);
//                    break;
//                case "Дата начала исполнения контракта":
//                case "Срок исполнения контракта":
//                case "Закупка за счет бюджетных средств":
//                case "Закупка за счет собственных средств организации":
//                case "Наименование бюджета":
//                case "Вид бюджета":
//                case "Код территории муниципального образования":
//                case "Финансовое обеспечение закупки (всего)":
//                case "Количество этапов":
//                    // обрабатывается в FundingSource()
//                    break;
//                case "Размер обеспечения заявки":
//                case "Размер обеспечения заявки на участие в электронном аукционе":
//                case "Размер обеспечения гарантийных обязательств":
//                case "Размер обеспечения исполнения контракта":
//                case "Размер обеспечения исполнения обязательств по договору":
//                case "Требуется обеспечение исполнения контракта":
//                case "Требуется обеспечение заявки":
//                case "Требуется обеспечение гарантийных обязательств":
//                case "Порядок предоставления обеспечения исполнения контракта, требования к обеспечению":
//                case "Порядок предоставления обеспечения гарантийных обязательств, требования к обеспечению":
//                case "Порядок внесения денежных средств в качестве обеспечения заявки на участие в закупке, а также условия гарантии":
//                case "Платежные реквизиты для обеспечения гарантийных обязательств":
//                case "Платежные реквизиты для обеспечения исполнения контракта":
//                case "Платежные реквизиты для перечисления денежных средств при уклонении участника закупки от заключения контракта":
//                case "Реквизиты счета в соответствии с п.16 ч.1 ст.42 Закона № 44-ФЗ":
//                    // обрабатывается в *Provides()
//                    break;
//                case "Невозможно определить количество (объем) закупаемых товаров, работ, услуг":
//                case "Закупка в соответствии с подпунктом «г» пункта 2 части 10 статьи 24 Закона № 44-ФЗ":
//                    // обрабатывается в TradeInfo*()
//                    break;
//                case "Наименование закупки":
//                case "Предмет электронного аукциона":
//                    // дубликат информации из хедера карточки. Эти данные уже есть под другими именами.
//                    break;
//                case "Порядок вскрытия конвертов":
//                case "Другие виды работ в соответствии с НПА субъекта РФ":
//                    // ???... что делать с этой информацией?
//                    break;
//                default:
//                    log.warn("Найдено необработанное поле " + key);
//            }
//        }
//    }

//    private void buildNoticeAuctionDate(String date, String time, Purchase purchase) {
//        if (date != null && time != null) {
//            purchase.setNoticeAuctionDate(UniversalDateParser.parseDateTime(date + " " + time));
//        }
//    }

//    private void taskAccomplishedUpdate(ParserThread parserThread) {
//        ParserTask task =
//            ((ParserTask) parserThread.get(ParserThread.ENVIRONMENTS.TASK)).increasePagesCount().increaseItemsCount().decreaseErrorCount();
//        parserStateService.updateTaskState(ParserEvents.CARD_PARSED.name(), ((ParserThread) Thread.currentThread()));
//        Selenide.sleep(props.getEachStateSleepMs());
//        // если успешный парсинг - уменьшаем количество ошибок (экспериментально):
//        if (task.getErrorCount() > 1) {
//            task.setErrorCount(task.getErrorCount() - 1);
//        }
//        parserTaskHistoryService.save(parserTaskMapper.toHistoryWithNullID(parserTaskService.saveAndFlush(task)));
//        ((ParserThread) Thread.currentThread()).store(ParserThread.ENVIRONMENTS.TASK, task);
//        ((ParserThread) Thread.currentThread()).getStateMachine().sendEvent(ParserEvents.CARD_PARSED);
//    }
}
